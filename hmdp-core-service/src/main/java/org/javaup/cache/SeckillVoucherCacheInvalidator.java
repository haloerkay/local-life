package org.javaup.cache;

import lombok.RequiredArgsConstructor;
import org.javaup.core.RedisKeyManage;
import org.javaup.redis.RedisCache;
import org.javaup.redis.RedisKeyBuild;
import org.springframework.stereotype.Component;

/** Caller holds the metadata write lock. Inventory has a separate lifecycle. */
@Component
@RequiredArgsConstructor
public class SeckillVoucherCacheInvalidator {
    private final RedisCache redis;
    private final SeckillVoucherLocalCache orderCache;
    private final VoucherDisplayCache displayCache;

    public void invalidate(Long id) {
        orderCache.invalidate(RedisKeyBuild.createRedisKey(RedisKeyManage.SECKILL_VOUCHER_TAG_KEY, id).getRelKey());
        displayCache.invalidate(id);
        // Clear display keys first so even a later transaction-cache deletion failure
        // does not prevent refreshing the display metadata.
        redis.del(VoucherDisplayCache.valueKey(id));
        redis.del(VoucherDisplayCache.nullKey(id));
        redis.del(RedisKeyBuild.createRedisKey(RedisKeyManage.SECKILL_VOUCHER_TAG_KEY, id));
        redis.del(RedisKeyBuild.createRedisKey(RedisKeyManage.SECKILL_VOUCHER_NULL_TAG_KEY, id));
        // Do not delete SECKILL_STOCK_TAG_KEY: it may contain in-flight reservations.
    }
}
