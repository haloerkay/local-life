package org.javaup.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.javaup.model.SeckillVoucherFullModel;
import org.javaup.redis.RedisKeyBuild;
import org.javaup.core.RedisKeyManage;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.function.Function;

/** Display metadata only. Never used as an inventory or purchase eligibility source. */
@Component
public class VoucherDisplayCache {
    public static final long REDIS_TTL_SECONDS = 60;
    private final Cache<Long, SeckillVoucherFullModel> local = Caffeine.newBuilder()
            .maximumSize(10000).expireAfterWrite(Duration.ofSeconds(5)).build();

    public SeckillVoucherFullModel get(Long id) { return local.getIfPresent(id); }
    public void put(Long id, SeckillVoucherFullModel value) { local.put(id, value); }
    public void invalidate(Long id) { local.invalidate(id); }

    // Call only while holding the metadata read lock: a writer must not wait for
    // this computation while the computation is waiting for that writer's lock.
    public SeckillVoucherFullModel getOrLoad(Long id, Function<Long, SeckillVoucherFullModel> loader) {
        return local.get(id, loader);
    }

    public static RedisKeyBuild valueKey(Long id) {
        return RedisKeyBuild.createRedisKey(RedisKeyManage.VOUCHER_DISPLAY_TAG_KEY, id);
    }
    public static RedisKeyBuild nullKey(Long id) {
        return RedisKeyBuild.createRedisKey(RedisKeyManage.VOUCHER_DISPLAY_NULL_TAG_KEY, id);
    }
}
