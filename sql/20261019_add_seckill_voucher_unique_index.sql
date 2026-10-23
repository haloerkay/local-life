-- Existing installations: run once against hmdp_0, after checking duplicates.
-- One voucher has at most one seckill configuration. This is not an order-table
-- user/voucher uniqueness constraint and does not change cancel-and-rebuy rules.
SELECT voucher_id, COUNT(*) AS duplicate_count
FROM tb_seckill_voucher
GROUP BY voucher_id HAVING COUNT(*) > 1;

ALTER TABLE tb_seckill_voucher
    ADD UNIQUE KEY uk_seckill_voucher_id (voucher_id);

-- Read-only verification; EXPLAIN should use the unique key for voucher_id lookup.
SHOW INDEX FROM tb_seckill_voucher;
-- Rollback, if explicitly needed later:
-- ALTER TABLE tb_seckill_voucher DROP INDEX uk_seckill_voucher_id;
