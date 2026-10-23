package org.javaup.benchmark;

import org.javaup.config.SeckillMessageTimingProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/** Only present in an explicitly enabled load-test application profile. */
@Component
@Profile("loadtest")
public class VoucherReadBenchmark {
    public enum Mode { MYSQL, REDIS, TIERED }

    private final Mode mode;
    private final String readPath;
    private final SeckillMessageTimingProperties messageTiming;
    private final Map<String, LongAdder> reads = new ConcurrentHashMap<>();

    public VoucherReadBenchmark(@Value("${loadtest.voucher-cache-mode:tiered}") String mode,
                                @Value("${loadtest.voucher-read-path:fast}") String readPath,
                                SeckillMessageTimingProperties messageTiming) {
        this.mode = Mode.valueOf(mode.trim().toUpperCase(Locale.ROOT));
        this.readPath = readPath.trim().toLowerCase(Locale.ROOT);
        this.messageTiming = messageTiming;
        if (!this.readPath.equals("fast") && !this.readPath.equals("locked")) {
            throw new IllegalArgumentException("voucher-read-path must be fast or locked");
        }
    }

    public boolean lockedReadPath() { return readPath.equals("locked"); }

    public Mode mode() {
        return mode;
    }

    public void record(String source) {
        reads.computeIfAbsent(source, ignored -> new LongAdder()).increment();
    }

    public Map<String, Object> snapshot() {
        Map<String, Long> counts = new TreeMap<>();
        reads.forEach((source, count) -> counts.put(source, count.sum()));
        return Map.of("mode", mode.name().toLowerCase(Locale.ROOT), "readPath", readPath,
                "successfulReadsBySource", counts, "displayLocalTtlSeconds", 5, "displayRedisTtlSeconds", 60,
                "databaseReadStrategy", "join",
                "messageMaxAgeMs", messageTiming.getMaxAgeMs(),
                "reconciliationWaitMs", messageTiming.getReconciliationWaitMs());
    }
}
