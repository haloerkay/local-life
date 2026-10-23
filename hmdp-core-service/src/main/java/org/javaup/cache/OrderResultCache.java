package org.javaup.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javaup.core.RedisKeyManage;
import org.javaup.entity.VoucherOrder;
import org.javaup.redis.RedisCache;
import org.javaup.redis.RedisKeyBuild;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.TimeUnit;

/** A best-effort cache of committed orders. MySQL remains the source of truth. */
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderResultCache {
    private final RedisCache redis;

    public void publishAfterCommit(VoucherOrder order) {
        if (!TransactionSynchronizationManager.isActualTransactionActive()
                || !TransactionSynchronizationManager.isSynchronizationActive()) {
            log.warn("订单结果缓存要求事务提交后发布，当前无事务，跳过缓存 orderId={}", order.getId());
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    redis.set(key(order.getId()), order, 60, TimeUnit.SECONDS);
                } catch (RuntimeException failure) {
                    // The database has committed; a cache outage must not trigger
                    // consumer failure compensation against a successful order.
                    log.warn("订单已提交，结果缓存写入失败，查询将回源数据库 orderId={}", order.getId(), failure);
                }
            }
        });
    }

    public VoucherOrder get(Long orderId) {
        try {
            return redis.get(key(orderId), VoucherOrder.class);
        } catch (RuntimeException failure) {
            log.warn("订单结果缓存读取失败，回源数据库 orderId={}", orderId, failure);
            return null;
        }
    }

    private RedisKeyBuild key(Long orderId) {
        return RedisKeyBuild.createRedisKey(RedisKeyManage.DB_SECKILL_ORDER_KEY, orderId);
    }
}
