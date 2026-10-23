package org.javaup.controller;

import org.javaup.benchmark.VoucherReadBenchmark;
import org.javaup.dto.Result;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Profile("loadtest")
@RestController
@RequestMapping("/voucher/benchmark")
public class VoucherReadBenchmarkController {
    private final VoucherReadBenchmark benchmark;

    public VoucherReadBenchmarkController(VoucherReadBenchmark benchmark) {
        this.benchmark = benchmark;
    }

    @GetMapping("/stats")
    public Result<Map<String, Object>> stats() {
        return Result.ok(benchmark.snapshot());
    }
}
