package org.javaup.config;

import org.javaup.benchmark.VoucherReadBenchmark;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.junit.jupiter.api.Assertions.*;

class SeckillMessageTimingPropertiesTest {
    private final ApplicationContextRunner context = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(ConfigurationPropertiesAutoConfiguration.class))
            .withUserConfiguration(SeckillMessageTimingProperties.class);

    @Test
    void defaultPreservesTenSecondPolicyAndGrace() {
        context.run(ctx -> {
            assertNull(ctx.getStartupFailure());
            var policy = ctx.getBean(SeckillMessageTimingProperties.class);
            assertEquals(10_000L, policy.getMaxAgeMs());
            assertEquals(12_000L, policy.getReconciliationWaitMs());
        });
    }

    @Test
    void fifteenSecondSettingAlsoUpdatesGraceAndReportedValues() {
        context.withPropertyValues("seckill.message.max-age-ms=15000").run(ctx -> {
            assertNull(ctx.getStartupFailure());
            var policy = ctx.getBean(SeckillMessageTimingProperties.class);
            assertEquals(15_000L, policy.getMaxAgeMs());
            assertEquals(17_000L, policy.getReconciliationWaitMs());
            var stats = new VoucherReadBenchmark("tiered", "fast", policy).snapshot();
            assertEquals(15_000L, stats.get("messageMaxAgeMs"));
            assertEquals(17_000L, stats.get("reconciliationWaitMs"));
        });
    }

    @Test
    void invalidZeroSettingFailsAtStartup() {
        context.withPropertyValues("seckill.message.max-age-ms=0")
                .run(ctx -> assertNotNull(ctx.getStartupFailure()));
    }

    @Test
    void negativeAndOverflowSettingsAreRejected() {
        var policy = new SeckillMessageTimingProperties();
        assertThrows(IllegalArgumentException.class, () -> policy.setMaxAgeMs(-1));
        assertThrows(IllegalArgumentException.class, () -> policy.setMaxAgeMs(Long.MAX_VALUE));
    }
}
