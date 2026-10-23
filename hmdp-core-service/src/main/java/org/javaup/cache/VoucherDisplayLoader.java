package org.javaup.cache;

import lombok.RequiredArgsConstructor;
import org.javaup.handler.BloomFilterHandlerFactory;
import org.javaup.mapper.SeckillVoucherMapper;
import org.javaup.model.SeckillVoucherFullModel;
import org.javaup.redis.RedisCache;
import org.javaup.servicelock.LockType;
import org.javaup.servicelock.annotion.ServiceLock;
import org.javaup.util.ServiceLockTool;
import org.redisson.api.RLock;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static org.javaup.constant.Constant.BLOOM_FILTER_HANDLER_VOUCHER;
import static org.javaup.constant.DistributedLockConstants.UPDATE_SECKILL_VOUCHER_LOCK;

/** Separate bean so the miss-path read lock is applied through the Spring proxy. */
@Component
@RequiredArgsConstructor
public class VoucherDisplayLoader {
    private final RedisCache redis;
    private final VoucherDisplayCache cache;
    private final SeckillVoucherMapper seckillMapper;
    private final BloomFilterHandlerFactory bloomFilters;
    private final ServiceLockTool locks;

    @ServiceLock(lockType = LockType.Read, name = UPDATE_SECKILL_VOUCHER_LOCK, keys = {"#p0"})
    public SeckillVoucherFullModel withReadLock(Long voucherId, Supplier<SeckillVoucherFullModel> read) {
        return read.get();
    }

    @ServiceLock(lockType = LockType.Read, name = UPDATE_SECKILL_VOUCHER_LOCK, keys = {"#p0"})
    public SeckillVoucherFullModel load(Long voucherId, boolean useLocal) {
        return useLocal ? cache.getOrLoad(voucherId, this::loadShared) : loadShared(voucherId);
    }

    private SeckillVoucherFullModel loadShared(Long id) {
        SeckillVoucherFullModel hit = redis.get(VoucherDisplayCache.valueKey(id), SeckillVoucherFullModel.class);
        if (hit != null) { return hit; }
        if (Boolean.TRUE.equals(redis.hasKey(VoucherDisplayCache.nullKey(id)))
                || !bloomFilters.get(BLOOM_FILTER_HANDLER_VOUCHER).contains(id.toString())) {
            throw new IllegalArgumentException("查询秒杀优惠券不存在");
        }
        RLock mutex = locks.getLock(LockType.Reentrant, "lock:voucher:display:rebuild", new String[]{id.toString()});
        mutex.lock();
        try {
            // Double check after the cross-instance mutex, including negative results.
            hit = redis.get(VoucherDisplayCache.valueKey(id), SeckillVoucherFullModel.class);
            if (hit != null) { return hit; }
            if (Boolean.TRUE.equals(redis.hasKey(VoucherDisplayCache.nullKey(id)))) {
                throw new IllegalArgumentException("查询秒杀优惠券不存在");
            }
            SeckillVoucherFullModel value = loadDatabase(id);
            if (value == null) {
                redis.set(VoucherDisplayCache.nullKey(id), "missing", 5, TimeUnit.SECONDS);
                throw new IllegalArgumentException("查询秒杀优惠券不存在");
            }
            long ttl = value.getEndTime() == null ? VoucherDisplayCache.REDIS_TTL_SECONDS
                    : Math.max(1, Math.min(VoucherDisplayCache.REDIS_TTL_SECONDS,
                    Duration.between(LocalDateTime.now(), value.getEndTime()).getSeconds()));
            redis.set(VoucherDisplayCache.valueKey(id), value, ttl, TimeUnit.SECONDS);
            return value;
        } finally {
            mutex.unlock();
        }
    }

    // Called either under the miss-path read lock or the loadtest MYSQL baseline lock.
    public SeckillVoucherFullModel loadDatabase(Long id) {
        return seckillMapper.selectFullModelByVoucherId(id);
    }
}
