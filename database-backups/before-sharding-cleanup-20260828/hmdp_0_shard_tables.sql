-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: hmdp_0
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tb_user_0`
--

DROP TABLE IF EXISTS `tb_user_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_user_0` (
  `id` bigint unsigned NOT NULL COMMENT '主键',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号码',
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '密码，加密存储',
  `nick_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '昵称，默认是用户id',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '人物头像',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniqe_key_phone` (`phone`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user_0`
--

LOCK TABLES `tb_user_0` WRITE;
/*!40000 ALTER TABLE `tb_user_0` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_user_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user_1`
--

DROP TABLE IF EXISTS `tb_user_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_user_1` (
  `id` bigint unsigned NOT NULL COMMENT '主键',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号码',
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '密码，加密存储',
  `nick_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '昵称，默认是用户id',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '人物头像',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniqe_key_phone` (`phone`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user_1`
--

LOCK TABLES `tb_user_1` WRITE;
/*!40000 ALTER TABLE `tb_user_1` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_user_1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user_info_0`
--

DROP TABLE IF EXISTS `tb_user_info_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_user_info_0` (
  `id` bigint unsigned NOT NULL COMMENT '主键',
  `user_id` bigint unsigned NOT NULL COMMENT '主键，用户id',
  `city` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '城市名称',
  `introduce` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '个人介绍，不要超过128个字符',
  `fans` int unsigned DEFAULT '0' COMMENT '粉丝数量',
  `followee` int unsigned DEFAULT '0' COMMENT '关注的人的数量',
  `gender` tinyint unsigned DEFAULT '0' COMMENT '性别，0：男，1：女',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `credits` int unsigned DEFAULT '0' COMMENT '积分',
  `level` tinyint unsigned DEFAULT '0' COMMENT '会员级别，0~9级,0代表未开通会员',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user_info_0`
--

LOCK TABLES `tb_user_info_0` WRITE;
/*!40000 ALTER TABLE `tb_user_info_0` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_user_info_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user_info_1`
--

DROP TABLE IF EXISTS `tb_user_info_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_user_info_1` (
  `id` bigint unsigned NOT NULL COMMENT '主键',
  `user_id` bigint unsigned NOT NULL COMMENT '主键，用户id',
  `city` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '城市名称',
  `introduce` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '个人介绍，不要超过128个字符',
  `fans` int unsigned DEFAULT '0' COMMENT '粉丝数量',
  `followee` int unsigned DEFAULT '0' COMMENT '关注的人的数量',
  `gender` tinyint unsigned DEFAULT '0' COMMENT '性别，0：男，1：女',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `credits` int unsigned DEFAULT '0' COMMENT '积分',
  `level` tinyint unsigned DEFAULT '0' COMMENT '会员级别，0~9级,0代表未开通会员',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user_info_1`
--

LOCK TABLES `tb_user_info_1` WRITE;
/*!40000 ALTER TABLE `tb_user_info_1` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_user_info_1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user_phone_0`
--

DROP TABLE IF EXISTS `tb_user_phone_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_user_phone_0` (
  `id` bigint NOT NULL COMMENT '主键id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `phone` varchar(512) NOT NULL COMMENT '手机号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `phone_idx` (`phone`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户手机表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user_phone_0`
--

LOCK TABLES `tb_user_phone_0` WRITE;
/*!40000 ALTER TABLE `tb_user_phone_0` DISABLE KEYS */;
INSERT INTO `tb_user_phone_0` VALUES (1987042234952056833,1987042234935279617,'13838411438','2025-11-07 22:19:20','2025-11-07 22:19:20'),(1987042505568550913,1987042505555968001,'13456789001','2025-11-07 22:20:25','2025-11-07 22:20:25');
/*!40000 ALTER TABLE `tb_user_phone_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user_phone_1`
--

DROP TABLE IF EXISTS `tb_user_phone_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_user_phone_1` (
  `id` bigint NOT NULL COMMENT '主键id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `phone` varchar(512) NOT NULL COMMENT '手机号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `phone_idx` (`phone`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户手机表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user_phone_1`
--

LOCK TABLES `tb_user_phone_1` WRITE;
/*!40000 ALTER TABLE `tb_user_phone_1` DISABLE KEYS */;
INSERT INTO `tb_user_phone_1` VALUES (1987041610910924802,1987041610793484289,'13686869696','2025-11-07 22:16:52','2025-11-07 22:16:52');
/*!40000 ALTER TABLE `tb_user_phone_1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_voucher_0`
--

DROP TABLE IF EXISTS `tb_voucher_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_voucher_0` (
  `id` bigint unsigned NOT NULL COMMENT '主键',
  `shop_id` bigint unsigned DEFAULT NULL COMMENT '商铺id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '代金券标题',
  `sub_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '副标题',
  `rules` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '使用规则',
  `pay_value` bigint unsigned NOT NULL COMMENT '支付金额，单位是分。例如200代表2元',
  `actual_value` bigint NOT NULL COMMENT '抵扣金额，单位是分。例如200代表2元',
  `type` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '0,普通券；1,秒杀券',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '1,上架; 2,下架; 3,过期',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_voucher_0`
--

LOCK TABLES `tb_voucher_0` WRITE;
/*!40000 ALTER TABLE `tb_voucher_0` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_voucher_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_voucher_1`
--

DROP TABLE IF EXISTS `tb_voucher_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_voucher_1` (
  `id` bigint unsigned NOT NULL COMMENT '主键',
  `shop_id` bigint unsigned DEFAULT NULL COMMENT '商铺id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '代金券标题',
  `sub_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '副标题',
  `rules` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '使用规则',
  `pay_value` bigint unsigned NOT NULL COMMENT '支付金额，单位是分。例如200代表2元',
  `actual_value` bigint NOT NULL COMMENT '抵扣金额，单位是分。例如200代表2元',
  `type` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '0,普通券；1,秒杀券',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '1,上架; 2,下架; 3,过期',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_voucher_1`
--

LOCK TABLES `tb_voucher_1` WRITE;
/*!40000 ALTER TABLE `tb_voucher_1` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_voucher_1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_seckill_voucher_0`
--

DROP TABLE IF EXISTS `tb_seckill_voucher_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_seckill_voucher_0` (
  `id` bigint NOT NULL,
  `voucher_id` bigint unsigned NOT NULL COMMENT '关联的优惠券的id',
  `init_stock` int NOT NULL COMMENT '初始化的库存',
  `stock` int NOT NULL COMMENT '库存',
  `allowed_levels` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '允许参与的会员等级，逗号分隔，如："1,2,3"',
  `min_level` int DEFAULT NULL COMMENT '最低会员等级',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `begin_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生效时间',
  `end_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '失效时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='秒杀优惠券表，与优惠券是一对一关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_seckill_voucher_0`
--

LOCK TABLES `tb_seckill_voucher_0` WRITE;
/*!40000 ALTER TABLE `tb_seckill_voucher_0` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_seckill_voucher_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_seckill_voucher_1`
--

DROP TABLE IF EXISTS `tb_seckill_voucher_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_seckill_voucher_1` (
  `id` bigint NOT NULL,
  `voucher_id` bigint unsigned NOT NULL COMMENT '关联的优惠券的id',
  `init_stock` int NOT NULL COMMENT '初始化的库存',
  `stock` int NOT NULL COMMENT '库存',
  `allowed_levels` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '允许参与的会员等级，逗号分隔，如："1,2,3"',
  `min_level` int DEFAULT NULL COMMENT '最低会员等级',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `begin_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生效时间',
  `end_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '失效时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='秒杀优惠券表，与优惠券是一对一关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_seckill_voucher_1`
--

LOCK TABLES `tb_seckill_voucher_1` WRITE;
/*!40000 ALTER TABLE `tb_seckill_voucher_1` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_seckill_voucher_1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_voucher_order_0`
--

DROP TABLE IF EXISTS `tb_voucher_order_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_voucher_order_0` (
  `id` bigint NOT NULL COMMENT '主键',
  `user_id` bigint unsigned NOT NULL COMMENT '下单的用户id',
  `voucher_id` bigint unsigned NOT NULL COMMENT '购买的代金券id',
  `pay_type` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '支付方式 1：余额支付；2：支付宝；3：微信',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '订单状态，1：正常；2：已取消；',
  `reconciliation_status` int NOT NULL DEFAULT '1' COMMENT '对账状态：1待处理；2异常；3不一致；4一致',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  `pay_time` timestamp NULL DEFAULT NULL COMMENT '支付时间',
  `use_time` timestamp NULL DEFAULT NULL COMMENT '核销时间',
  `refund_time` timestamp NULL DEFAULT NULL COMMENT '退款时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_voucher_order_0`
--

LOCK TABLES `tb_voucher_order_0` WRITE;
/*!40000 ALTER TABLE `tb_voucher_order_0` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_voucher_order_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_voucher_order_1`
--

DROP TABLE IF EXISTS `tb_voucher_order_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_voucher_order_1` (
  `id` bigint NOT NULL COMMENT '主键',
  `user_id` bigint unsigned NOT NULL COMMENT '下单的用户id',
  `voucher_id` bigint unsigned NOT NULL COMMENT '购买的代金券id',
  `pay_type` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '支付方式 1：余额支付；2：支付宝；3：微信',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '订单状态，1：正常；2：已取消；',
  `reconciliation_status` tinyint NOT NULL DEFAULT '1' COMMENT '对账状态：1待处理；2异常；3不一致；4一致',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  `pay_time` timestamp NULL DEFAULT NULL COMMENT '支付时间',
  `use_time` timestamp NULL DEFAULT NULL COMMENT '核销时间',
  `refund_time` timestamp NULL DEFAULT NULL COMMENT '退款时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_voucher_order_1`
--

LOCK TABLES `tb_voucher_order_1` WRITE;
/*!40000 ALTER TABLE `tb_voucher_order_1` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_voucher_order_1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_voucher_order_router_0`
--

DROP TABLE IF EXISTS `tb_voucher_order_router_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_voucher_order_router_0` (
  `id` bigint NOT NULL COMMENT '主键',
  `order_id` bigint NOT NULL COMMENT '订单id',
  `user_id` bigint unsigned NOT NULL COMMENT '用户id',
  `voucher_id` bigint unsigned NOT NULL COMMENT '代金券id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_voucher_order_router_0`
--

LOCK TABLES `tb_voucher_order_router_0` WRITE;
/*!40000 ALTER TABLE `tb_voucher_order_router_0` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_voucher_order_router_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_voucher_order_router_1`
--

DROP TABLE IF EXISTS `tb_voucher_order_router_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_voucher_order_router_1` (
  `id` bigint NOT NULL COMMENT '主键',
  `order_id` bigint NOT NULL COMMENT '订单id',
  `user_id` bigint unsigned NOT NULL COMMENT '用户id',
  `voucher_id` bigint unsigned NOT NULL COMMENT '代金券id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_voucher_order_router_1`
--

LOCK TABLES `tb_voucher_order_router_1` WRITE;
/*!40000 ALTER TABLE `tb_voucher_order_router_1` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_voucher_order_router_1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_voucher_reconcile_log_0`
--

DROP TABLE IF EXISTS `tb_voucher_reconcile_log_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_voucher_reconcile_log_0` (
  `id` bigint NOT NULL COMMENT '主键',
  `order_id` bigint NOT NULL COMMENT '订单id',
  `user_id` bigint unsigned NOT NULL COMMENT '下单的用户id',
  `voucher_id` bigint unsigned NOT NULL COMMENT '购买的代金券id',
  `message_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Kafka消息uuid',
  `detail` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '差异说明',
  `before_qty` int DEFAULT NULL COMMENT '改变之前库存数量',
  `change_qty` int DEFAULT NULL COMMENT '本次改变数量',
  `after_qty` int DEFAULT NULL COMMENT '改变之后库存数量',
  `trace_id` bigint DEFAULT NULL COMMENT '追踪唯一标识',
  `log_type` int DEFAULT '-1' COMMENT '记录类型 -1:扣减 1:恢复',
  `business_type` int DEFAULT '1' COMMENT '业务类型：1创建订单成功；2创建订单超时；3创建订单失败',
  `reconciliation_status` int NOT NULL DEFAULT '1' COMMENT '对账状态：1待处理；2异常；3不一致；4一致',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE,
  KEY `idx_message_id` (`message_id`) USING BTREE,
  KEY `idx_trace_id` (`trace_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_voucher_reconcile_log_0`
--

LOCK TABLES `tb_voucher_reconcile_log_0` WRITE;
/*!40000 ALTER TABLE `tb_voucher_reconcile_log_0` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_voucher_reconcile_log_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_voucher_reconcile_log_1`
--

DROP TABLE IF EXISTS `tb_voucher_reconcile_log_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_voucher_reconcile_log_1` (
  `id` bigint NOT NULL COMMENT '主键',
  `order_id` bigint NOT NULL COMMENT '订单id',
  `user_id` bigint unsigned NOT NULL COMMENT '下单的用户id',
  `voucher_id` bigint unsigned NOT NULL COMMENT '购买的代金券id',
  `message_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Kafka消息uuid',
  `detail` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '差异说明',
  `before_qty` int DEFAULT NULL COMMENT '改变之前库存数量',
  `change_qty` int DEFAULT NULL COMMENT '本次改变数量',
  `after_qty` int DEFAULT NULL COMMENT '改变之后库存数量',
  `trace_id` bigint DEFAULT NULL COMMENT '追踪唯一标识',
  `log_type` int DEFAULT '-1' COMMENT '记录类型 -1:扣减 1:恢复',
  `business_type` int DEFAULT '1' COMMENT '业务类型：1创建订单成功；2创建订单超时；3创建订单失败',
  `reconciliation_status` int NOT NULL DEFAULT '1' COMMENT '对账状态：1待处理；2异常；3不一致；4一致',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE,
  KEY `idx_message_id` (`message_id`) USING BTREE,
  KEY `idx_trace_id` (`trace_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_voucher_reconcile_log_1`
--

LOCK TABLES `tb_voucher_reconcile_log_1` WRITE;
/*!40000 ALTER TABLE `tb_voucher_reconcile_log_1` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_voucher_reconcile_log_1` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-08-28 19:44:16
