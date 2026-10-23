package org.javaup.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** Shared message-age policy; preserves the original 10s default and 2s grace. */
@Component
@ConfigurationProperties(prefix = "seckill.message")
public class SeckillMessageTimingProperties {
    private long maxAgeMs = 10_000L;

    public long getMaxAgeMs() {
        return maxAgeMs;
    }

    public void setMaxAgeMs(long maxAgeMs) {
        if (maxAgeMs <= 0 || maxAgeMs > Long.MAX_VALUE - 2_000L) {
            throw new IllegalArgumentException("seckill.message.max-age-ms must be positive and allow the 2s grace");
        }
        this.maxAgeMs = maxAgeMs;
    }

    public long getReconciliationWaitMs() {
        return maxAgeMs + 2_000L;
    }
}
