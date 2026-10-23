package org.javaup.cache;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;
import org.javaup.mapper.SeckillVoucherMapper;
import org.javaup.model.SeckillVoucherFullModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/** Opt-in read-only integration check: no Boot startup initializers are run. */
@EnabledIfEnvironmentVariable(named = "HMDP_VERIFY_DATABASE", matches = "true")
class SeckillVoucherJoinMappingTest {
    @Test
    void joinedProjectionMatchesOriginalFieldsAndNeverExposesLiveStock() throws Exception {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("application.yml"));
        Properties properties = yaml.getObject();
        assertNotNull(properties);
        String url = properties.getProperty("spring.datasource.url");
        assertTrue(url.startsWith("jdbc:mysql://localhost:") || url.startsWith("jdbc:mysql://127.0.0.1:"));
        UnpooledDataSource dataSource = new UnpooledDataSource("com.mysql.cj.jdbc.Driver", url,
                properties.getProperty("spring.datasource.username"), properties.getProperty("spring.datasource.password"));
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setEnvironment(new Environment("join-readonly", new JdbcTransactionFactory(), dataSource));
        configuration.addMapper(SeckillVoucherMapper.class);
        var factory = new MybatisSqlSessionFactoryBuilder().build(configuration);
        try (var session = factory.openSession()) {
            Connection connection = session.getConnection();
            connection.setReadOnly(true);
            var ids = new ArrayList<Long>();
            try (var statement = connection.createStatement();
                 var rows = statement.executeQuery("SELECT voucher_id FROM tb_seckill_voucher")) {
                while (rows.next()) { ids.add(rows.getLong(1)); }
            }
            assertFalse(ids.isEmpty(), "Expected existing local test vouchers");
            var mapper = session.getMapper(SeckillVoucherMapper.class);
            for (Long id : ids) {
                SeckillVoucherFullModel joined = mapper.selectFullModelByVoucherId(id);
                try (var statement = connection.prepareStatement("SELECT * FROM tb_seckill_voucher WHERE voucher_id=?")) {
                    statement.setLong(1, id);
                    try (var row = statement.executeQuery()) {
                        assertTrue(row.next());
                        assertNotNull(joined);
                        assertEquals(row.getLong("id"), joined.getId());
                        assertEquals(id, joined.getVoucherId());
                        assertEquals(row.getInt("init_stock"), joined.getInitStock());
                        assertEquals(row.getString("allowed_levels"), joined.getAllowedLevels());
                        assertEquals(row.getObject("min_level", Integer.class), joined.getMinLevel());
                        assertEquals(row.getTimestamp("create_time").toLocalDateTime(), joined.getCreateTime());
                        assertEquals(row.getTimestamp("begin_time").toLocalDateTime(), joined.getBeginTime());
                        assertEquals(row.getTimestamp("end_time").toLocalDateTime(), joined.getEndTime());
                        assertNull(joined.getStock());
                    }
                }
                try (var statement = connection.prepareStatement("SELECT shop_id,status FROM tb_voucher WHERE id=?")) {
                    statement.setLong(1, id);
                    try (var row = statement.executeQuery()) {
                        assertTrue(row.next());
                        assertEquals(row.getLong("shop_id"), joined.getShopId());
                        assertEquals(row.getInt("status"), joined.getStatus());
                    }
                }
            }
            assertNull(mapper.selectFullModelByVoucherId(-1L));
        }
    }
}
