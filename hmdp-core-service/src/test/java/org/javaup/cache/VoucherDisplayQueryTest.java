package org.javaup.cache;

import org.javaup.benchmark.VoucherReadBenchmark;
import org.javaup.config.SeckillMessageTimingProperties;
import org.javaup.core.SpringUtil;
import org.javaup.handler.BloomFilterHandlerFactory;
import org.javaup.kafka.producer.SeckillVoucherInvalidationProducer;
import org.javaup.mapper.SeckillVoucherMapper;
import org.javaup.model.SeckillVoucherFullModel;
import org.javaup.redis.RedisCache;
import org.javaup.redis.RedisKeyBuild;
import org.javaup.util.ServiceLockTool;
import org.javaup.servicelock.ServiceLocker;
import org.javaup.servicelock.LockType;
import org.javaup.servicelock.aspect.ServiceLockAspect;
import org.javaup.servicelock.factory.ServiceLockFactory;
import org.javaup.lockinfo.factory.LockInfoHandleFactory;
import org.javaup.lockinfo.impl.ServiceLockInfoHandle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.redisson.api.RLock;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class VoucherDisplayQueryTest {
    private GenericApplicationContext context;
    private Object previousContext;

    @BeforeEach
    void initializeKeyPrefix() {
        previousContext = ReflectionTestUtils.getField(SpringUtil.class, "configurableApplicationContext");
        context = new GenericApplicationContext();
        new SpringUtil().initialize(context);
    }

    @AfterEach
    void restoreKeyPrefix() {
        ReflectionTestUtils.setField(SpringUtil.class, "configurableApplicationContext", previousContext);
        context.close();
    }
    private final VoucherDisplayCache cache = new VoucherDisplayCache();
    private final RedisCache redis = mock(RedisCache.class);
    private final ServiceLockTool locks = mock(ServiceLockTool.class);
    private final VoucherDisplayLoader loader = mock(VoucherDisplayLoader.class);

    @SuppressWarnings("unchecked")
    private VoucherDisplayQueryService service(String path) {
        ObjectProvider<VoucherReadBenchmark> provider = mock(ObjectProvider.class);
        when(provider.getIfAvailable()).thenReturn(new VoucherReadBenchmark("tiered", path,
                new SeckillMessageTimingProperties()));
        return new VoucherDisplayQueryService(cache, redis, loader, provider);
    }

    @Test
    void localHitDoesNotDependOnRedisOrDistributedLocks() {
        SeckillVoucherFullModel value = new SeckillVoucherFullModel();
        cache.put(7L, value);
        assertSame(value, service("fast").query(7L));
        verifyNoInteractions(redis, locks, loader);
    }

    @Test
    void redisHitWarmsLocalWithoutDistributedReadLock() {
        SeckillVoucherFullModel value = new SeckillVoucherFullModel();
        when(redis.get(any(RedisKeyBuild.class), eq(SeckillVoucherFullModel.class))).thenReturn(value);
        VoucherDisplayQueryService service = service("fast");
        assertSame(value, service.query(7L));
        assertSame(value, service.query(7L));
        verify(redis, times(1)).get(any(RedisKeyBuild.class), eq(SeckillVoucherFullModel.class));
        verifyNoInteractions(locks, loader);
    }

    @Test
    void baselineStillAcquiresAndReleasesReadLockForSameLocalHit() {
        when(loader.withReadLock(eq(7L), any())).thenAnswer(invocation ->
                invocation.<java.util.function.Supplier<SeckillVoucherFullModel>>getArgument(1).get());
        cache.put(7L, new SeckillVoucherFullModel());
        service("locked").query(7L);
        verify(loader).withReadLock(eq(7L), any());
        verifyNoInteractions(redis, locks);
    }

    @Test
    void missPathUsesSpringProxyAndTheMetadataLockNamespace() {
        LockInfoHandleFactory names = mock(LockInfoHandleFactory.class);
        when(names.getLockInfoHandle(any())).thenReturn(new ServiceLockInfoHandle());
        ServiceLockFactory lockFactory = mock(ServiceLockFactory.class);
        ServiceLocker reader = mock(ServiceLocker.class);
        when(lockFactory.getLock(LockType.Read)).thenReturn(reader);
        java.util.concurrent.atomic.AtomicBoolean held = new java.util.concurrent.atomic.AtomicBoolean();
        when(reader.tryLock(anyString(), any(), anyLong())).thenAnswer(call -> {
            held.set(true);
            return true;
        });
        doAnswer(call -> { held.set(false); return null; }).when(reader).unlock(anyString());
        VoucherDisplayLoader target = new VoucherDisplayLoader(redis, cache, null, null, locks);
        AspectJProxyFactory factory = new AspectJProxyFactory(target);
        factory.setProxyTargetClass(true);
        factory.addAspect(new ServiceLockAspect(names, lockFactory));
        VoucherDisplayLoader proxy = factory.getProxy();
        SeckillVoucherFullModel value = new SeckillVoucherFullModel();
        assertSame(value, proxy.withReadLock(7L, () -> { assertTrue(held.get()); return value; }));
        assertFalse(held.get());
        var key = org.mockito.ArgumentCaptor.forClass(String.class);
        verify(reader).tryLock(key.capture(), any(), anyLong());
        assertTrue(key.getValue().contains("SERVICE_LOCK"));
        assertTrue(key.getValue().endsWith("7"));
        verify(reader).unlock(key.getValue());
        // Actual cold-load method must also pass the proxy before checking the cache.
        cache.put(7L, value);
        assertSame(value, proxy.load(7L, true));
        verify(reader, times(2)).tryLock(eq(key.getValue()), any(), anyLong());
    }

    @Test
    void coldConcurrentReadersCoalesceDatabaseLoad() throws Exception {
        SeckillVoucherMapper seckill = mock(SeckillVoucherMapper.class);
        BloomFilterHandlerFactory bloom = mock(BloomFilterHandlerFactory.class, RETURNS_DEEP_STUBS);
        when(bloom.get(anyString()).contains(anyString())).thenReturn(true);
        when(locks.getLock(any(), anyString(), any(String[].class))).thenReturn(mock(RLock.class));
        SeckillVoucherFullModel metadata = new SeckillVoucherFullModel();
        metadata.setVoucherId(7L);
        metadata.setEndTime(LocalDateTime.now().plusDays(1));
        when(seckill.selectFullModelByVoucherId(7L)).thenAnswer(invocation -> {
            Thread.sleep(30);
            return metadata;
        });
        VoucherDisplayLoader realLoader = new VoucherDisplayLoader(redis, cache, seckill, bloom, locks);
        ExecutorService pool = Executors.newFixedThreadPool(8);
        CountDownLatch start = new CountDownLatch(1);
        try {
            List<Future<SeckillVoucherFullModel>> futures = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                futures.add(pool.submit(() -> { start.await(); return realLoader.load(7L, true); }));
            }
            start.countDown();
            for (var result : futures) { assertNull(result.get(5, TimeUnit.SECONDS).getStock()); }
            verify(seckill, times(1)).selectFullModelByVoucherId(7L);
            verify(redis).set(any(RedisKeyBuild.class), any(SeckillVoucherFullModel.class), eq(60L), eq(TimeUnit.SECONDS));
        } finally {
            pool.shutdownNow();
        }
    }

    @Test
    void metadataInvalidationDoesNotDeleteReservedInventory() {
        SeckillVoucherCacheInvalidator invalidator = new SeckillVoucherCacheInvalidator(
                redis, mock(SeckillVoucherLocalCache.class), cache);
        cache.put(7L, new SeckillVoucherFullModel());
        invalidator.invalidate(7L);
        assertNull(cache.get(7L));
        var keys = org.mockito.ArgumentCaptor.forClass(RedisKeyBuild.class);
        verify(redis, times(4)).del(keys.capture());
        assertTrue(keys.getAllValues().stream().noneMatch(key -> key.getRelKey().contains("seckill:stock:")));
    }

    @Test
    void updatePublishesOnlyAfterCommitAndNotOnRollback() {
        SeckillVoucherCacheInvalidator invalidator = mock(SeckillVoucherCacheInvalidator.class);
        SeckillVoucherInvalidationProducer producer = mock(SeckillVoucherInvalidationProducer.class);
        SeckillVoucherCacheInvalidationPublisher publisher = new SeckillVoucherCacheInvalidationPublisher();
        ReflectionTestUtils.setField(publisher, "invalidator", invalidator);
        ReflectionTestUtils.setField(publisher, "invalidationProducer", producer);
            TransactionSynchronizationManager.initSynchronization();
            TransactionSynchronizationManager.setActualTransactionActive(true);
            try {
                publisher.publishInvalidate(7L, "update");
                verifyNoInteractions(invalidator, producer);
                for (var hook : TransactionSynchronizationManager.getSynchronizations()) {
                    hook.afterCompletion(TransactionSynchronization.STATUS_ROLLED_BACK);
                }
                verifyNoInteractions(invalidator, producer);
            } finally { TransactionSynchronizationManager.clear(); }
            TransactionSynchronizationManager.initSynchronization();
            TransactionSynchronizationManager.setActualTransactionActive(true);
            try {
                publisher.publishInvalidate(7L, "update");
                verifyNoInteractions(invalidator, producer);
                for (var hook : TransactionSynchronizationManager.getSynchronizations()) { hook.afterCommit(); }
                verify(invalidator).invalidate(7L);
                verify(producer).sendPayload(anyString(), any());
            } finally { TransactionSynchronizationManager.clear(); }
    }
}
