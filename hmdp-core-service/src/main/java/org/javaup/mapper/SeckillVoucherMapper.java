package org.javaup.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.javaup.entity.SeckillVoucher;
import org.javaup.model.SeckillVoucherFullModel;

public interface SeckillVoucherMapper extends BaseMapper<SeckillVoucher> {

    // Keep the DTO projection identical for display reads and transaction metadata.
    // Live stock is intentionally not part of this cacheable metadata projection.
    @Select("""
            SELECT sv.id,
                   sv.voucher_id AS voucherId,
                   sv.init_stock AS initStock,
                   sv.allowed_levels AS allowedLevels,
                   sv.min_level AS minLevel,
                   sv.create_time AS createTime,
                   sv.begin_time AS beginTime,
                   sv.end_time AS endTime,
                   v.status,
                   v.shop_id AS shopId
            FROM tb_seckill_voucher sv
            INNER JOIN tb_voucher v ON v.id = sv.voucher_id
            WHERE sv.voucher_id = #{voucherId}
            """)
    SeckillVoucherFullModel selectFullModelByVoucherId(@Param("voucherId") Long voucherId);
   
    @Update("UPDATE tb_seckill_voucher SET stock = stock + 1,update_time = NOW() WHERE voucher_id = #{voucherId}")
    Integer rollbackStock(@Param("voucherId")Long voucherId);

}
