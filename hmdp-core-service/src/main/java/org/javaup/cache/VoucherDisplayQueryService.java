package org.javaup.cache;

import org.javaup.benchmark.VoucherReadBenchmark;
import org.javaup.model.SeckillVoucherFullModel;
import org.javaup.redis.RedisCache;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;


@Service
public class VoucherDisplayQueryService {
    private final VoucherDisplayCache cache;
    private final RedisCache redis;
    private final VoucherDisplayLoader loader;
    private final VoucherReadBenchmark benchmark;

    public VoucherDisplayQueryService(VoucherDisplayCache cache, RedisCache redis,
                                     VoucherDisplayLoader loader,
                                     ObjectProvider<VoucherReadBenchmark> benchmark) {
        this.cache = cache;
        this.redis = redis;
        this.loader = loader;
        this.benchmark = benchmark.getIfAvailable();
    }

    public SeckillVoucherFullModel query(Long id) {
        if (benchmark != null && benchmark.lockedReadPath()) {
            return loader.withReadLock(id, () -> queryCache(id));
        }
        return queryCache(id);
    }

    private SeckillVoucherFullModel queryCache(Long id) {
        VoucherReadBenchmark.Mode mode = benchmark == null ? VoucherReadBenchmark.Mode.TIERED : benchmark.mode();
        if (mode == VoucherReadBenchmark.Mode.MYSQL) {
            SeckillVoucherFullModel value = loader.loadDatabase(id);
            if (value == null) { throw new IllegalArgumentException("查询秒杀优惠券不存在"); }
            record("MYSQL");
            return value;
        }
        if (mode == VoucherReadBenchmark.Mode.TIERED) {
            SeckillVoucherFullModel hit = cache.get(id);
            if (hit != null) { record("CAFFEINE"); return hit; }
        }
        SeckillVoucherFullModel hit = redis.get(VoucherDisplayCache.valueKey(id), SeckillVoucherFullModel.class);
        if (hit != null) {
            if (mode == VoucherReadBenchmark.Mode.TIERED) { cache.put(id, hit); }
            record("REDIS");
            return hit;
        }
        SeckillVoucherFullModel value = loader.load(id, mode == VoucherReadBenchmark.Mode.TIERED);
        record("REBUILD");
        return value;
    }

    private void record(String source) {
        if (benchmark != null) { benchmark.record(source); }
    }
}
