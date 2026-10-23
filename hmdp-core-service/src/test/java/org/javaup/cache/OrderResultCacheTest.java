package org.javaup.cache;

import org.javaup.core.SpringUtil;
import org.javaup.entity.VoucherOrder;
import org.javaup.redis.RedisCache;
import org.javaup.redis.RedisKeyBuild;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OrderResultCacheTest {
    private GenericApplicationContext context;
    private Object previousContext;
    private final RedisCache redis = mock(RedisCache.class);
    private final OrderResultCache cache = new OrderResultCache(redis);
    private final VoucherOrder order = new VoucherOrder().setId(7L).setUserId(8L).setVoucherId(9L);
    private final TransactionTemplate transactions = new TransactionTemplate(new LocalTransactionManager());

    @BeforeEach
    void initializePrefix() {
        previousContext = ReflectionTestUtils.getField(SpringUtil.class, "configurableApplicationContext");
        context = new GenericApplicationContext();
        new SpringUtil().initialize(context);
    }

    @AfterEach
    void restorePrefix() {
        ReflectionTestUtils.setField(SpringUtil.class, "configurableApplicationContext", previousContext);
        context.close();
    }

    @Test
    void onlyPublishesAfterSuccessfulCommit() {
        transactions.executeWithoutResult(status -> {
            cache.publishAfterCommit(order);
            verifyNoInteractions(redis);
        });
        verify(redis).set(any(RedisKeyBuild.class), same(order), eq(60L), eq(TimeUnit.SECONDS));
    }

    @Test
    void rollbackDoesNotPublishSuccess() {
        assertThrows(IllegalStateException.class, () -> transactions.executeWithoutResult(status -> {
            cache.publishAfterCommit(order);
            throw new IllegalStateException("simulated reconciliation-log write failure");
        }));
        verifyNoInteractions(redis);
    }

    @Test
    void cacheFailureDoesNotTurnCommittedOrderIntoConsumerFailure() {
        doThrow(new IllegalStateException("Redis unavailable")).when(redis)
                .set(any(RedisKeyBuild.class), same(order), eq(60L), eq(TimeUnit.SECONDS));
        assertDoesNotThrow(() -> transactions.executeWithoutResult(status -> cache.publishAfterCommit(order)));
    }

    @Test
    void readFailureBecomesCacheMissForDatabaseFallback() {
        when(redis.get(any(RedisKeyBuild.class), eq(VoucherOrder.class)))
                .thenThrow(new IllegalStateException("Redis unavailable"));
        assertNull(cache.get(7L));
    }

    @Test
    void refusesToPublishWithoutACommitBoundary() {
        cache.publishAfterCommit(order);
        verifyNoInteractions(redis);
    }

    // Exercises Spring's real commit/rollback synchronization lifecycle without a
    // live application context or business-data writes.
    private static class LocalTransactionManager extends AbstractPlatformTransactionManager {
        @Override protected Object doGetTransaction() { return new Object(); }
        @Override protected void doBegin(Object transaction, TransactionDefinition definition) { }
        @Override protected void doCommit(DefaultTransactionStatus status) { }
        @Override protected void doRollback(DefaultTransactionStatus status) { }
    }
}
