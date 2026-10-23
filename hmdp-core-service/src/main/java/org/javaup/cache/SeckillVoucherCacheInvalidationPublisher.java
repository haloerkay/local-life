package org.javaup.cache;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.javaup.kafka.message.SeckillVoucherInvalidationMessage;
import org.javaup.kafka.producer.SeckillVoucherInvalidationProducer;
import org.javaup.core.SpringUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.javaup.constant.Constant.SECKILL_VOUCHER_CACHE_INVALIDATION_TOPIC;

@Component
@Slf4j
public class SeckillVoucherCacheInvalidationPublisher {

    @Resource
    private SeckillVoucherCacheInvalidator invalidator;
    
    @Resource
    private SeckillVoucherInvalidationProducer invalidationProducer;
    
    public void publishInvalidate(Long voucherId, String reason) {
        if (TransactionSynchronizationManager.isActualTransactionActive()
                && TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() { invalidateAndPublish(voucherId, reason); }
            });
        } else {
            invalidateAndPublish(voucherId, reason);
        }
    }

    private void invalidateAndPublish(Long voucherId, String reason) {
        // The database is already committed. Report invalidation failure rather than
        // telling the caller that the database write rolled back. Still try broadcast.
        try {
            invalidator.invalidate(voucherId);
        } catch (RuntimeException failure) {
            log.error("券信息已提交，但本实例缓存失效失败 voucherId={}", voucherId, failure);
        }
        SeckillVoucherInvalidationMessage payload = new SeckillVoucherInvalidationMessage(voucherId, reason);
        try {
            invalidationProducer.sendPayload(
                    SpringUtil.getPrefixDistinctionName() + "-" + SECKILL_VOUCHER_CACHE_INVALIDATION_TOPIC, payload);
        } catch (RuntimeException failure) {
            log.error("券信息已提交，但缓存失效广播发送失败 voucherId={}", voucherId, failure);
        }
    }
}
