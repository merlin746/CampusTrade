-- ============================================
-- 校园二手交易平台 - 数据库初始化脚本
-- 使用方法: 在 MySQL 中执行此脚本即可创建数据库和表
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS school_trade
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE school_trade;

-- ============================================
-- 1. 用户表
-- ============================================
CREATE TABLE IF NOT EXISTS `user` (
  `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username`     VARCHAR(50)  NOT NULL COMMENT '用户名',
  `password`     VARCHAR(200) NOT NULL COMMENT '密码 (BCrypt加密)',
  `email`        VARCHAR(100)          DEFAULT NULL COMMENT '邮箱',
  `phone`        VARCHAR(20)           DEFAULT NULL COMMENT '手机号',
  `avatar`       VARCHAR(255)          DEFAULT NULL COMMENT '头像URL',
  `student_id`   VARCHAR(30)           DEFAULT NULL COMMENT '学号',
  `id_card_image` VARCHAR(255)         DEFAULT NULL COMMENT '学生证/身份证图片',
  `role`         TINYINT      NOT NULL DEFAULT 0 COMMENT '角色: 0=普通用户, 1=管理员',
  `status`       TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 0=禁用, 1=正常',
  `credit_score` INT          NOT NULL DEFAULT 100 COMMENT '信誉分 (0-1000)',
  `create_time`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_role_status` (`role`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ============================================
-- 2. 商品分类表
-- ============================================
CREATE TABLE IF NOT EXISTS `category` (
  `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name`        VARCHAR(50) NOT NULL COMMENT '分类名称',
  `icon`        VARCHAR(255)         DEFAULT NULL COMMENT '分类图标',
  `sort_order`  INT         NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品分类表';

-- ============================================
-- 3. 商品表
-- ============================================
CREATE TABLE IF NOT EXISTS `goods` (
  `id`             BIGINT       NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `title`          VARCHAR(200) NOT NULL COMMENT '商品标题',
  `description`    TEXT                  DEFAULT NULL COMMENT '商品描述',
  `price`          DECIMAL(10,2) NOT NULL COMMENT '售价',
  `original_price` DECIMAL(10,2)         DEFAULT NULL COMMENT '原价',
  `category_id`    BIGINT       NOT NULL COMMENT '分类ID',
  `seller_id`      BIGINT       NOT NULL COMMENT '卖家ID',
  `condition_level` TINYINT     NOT NULL DEFAULT 1 COMMENT '成色: 1=全新, 2=几乎全新, 3=有使用痕迹, 4=明显瑕疵',
  `status`         TINYINT      NOT NULL DEFAULT 0 COMMENT '状态: 0=待审核, 1=在售, 2=已售出, 3=已下架, 4=审核不通过',
  `view_count`     INT          NOT NULL DEFAULT 0 COMMENT '浏览量',
  `favorite_count` INT          NOT NULL DEFAULT 0 COMMENT '收藏数',
  `review_count`   INT          NOT NULL DEFAULT 0 COMMENT '评论数',
  `avg_score`      DECIMAL(2,1)          DEFAULT 0 COMMENT '平均评分 (1-5)',
  `address`        VARCHAR(200)          DEFAULT NULL COMMENT '交易地点 (如: 3号宿舍楼)',
  `contact_type`   TINYINT               DEFAULT 0 COMMENT '联系方式: 0=站内, 1=QQ, 2=微信, 3=手机号',
  `contact_info`   VARCHAR(50)           DEFAULT NULL COMMENT '联系方式详情',
  `is_reported`    TINYINT      NOT NULL DEFAULT 0 COMMENT '举报状态: 0=正常, 1=被举报',
  `create_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_seller` (`seller_id`),
  KEY `idx_category` (`category_id`),
  KEY `idx_status` (`status`),
  KEY `idx_title` (`title`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品表';

-- ============================================
-- 4. 商品图片表
-- ============================================
CREATE TABLE IF NOT EXISTS `goods_image` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `goods_id`    BIGINT       NOT NULL COMMENT '商品ID',
  `image_url`   VARCHAR(500) NOT NULL COMMENT '图片URL',
  `sort_order`  INT          NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品图片表';

-- ============================================
-- 5. 收藏表
-- ============================================
CREATE TABLE IF NOT EXISTS `favorite` (
  `id`          BIGINT   NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id`     BIGINT   NOT NULL COMMENT '用户ID',
  `goods_id`    BIGINT   NOT NULL COMMENT '商品ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_goods` (`user_id`, `goods_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收藏表';

-- ============================================
-- 6. 订单表
-- ============================================
CREATE TABLE IF NOT EXISTS `orders` (
  `id`              BIGINT        NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no`        VARCHAR(32)   NOT NULL COMMENT '订单号 (格式: OT + 时间戳)',
  `goods_id`        BIGINT        NOT NULL COMMENT '商品ID',
  `buyer_id`        BIGINT        NOT NULL COMMENT '买家ID',
  `seller_id`       BIGINT        NOT NULL COMMENT '卖家ID',
  `price`           DECIMAL(10,2) NOT NULL COMMENT '成交价格',
  `status`          TINYINT       NOT NULL DEFAULT 0 COMMENT '状态: 0=待确认, 1=已确认, 2=已完成, 3=已取消',
  `cancel_reason`   VARCHAR(200)           DEFAULT NULL COMMENT '取消原因',
  `buyer_reviewed`  TINYINT       NOT NULL DEFAULT 0 COMMENT '买家是否已评价: 0=未评, 1=已评',
  `seller_reviewed` TINYINT       NOT NULL DEFAULT 0 COMMENT '卖家是否已评价: 0=未评, 1=已评',
  `complete_time`   DATETIME               DEFAULT NULL COMMENT '完成时间',
  `create_time`     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_buyer` (`buyer_id`),
  KEY `idx_seller` (`seller_id`),
  KEY `idx_goods` (`goods_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- ============================================
-- 7. 评价表
-- ============================================
CREATE TABLE IF NOT EXISTS `review` (
  `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `order_id`     BIGINT       NOT NULL COMMENT '订单ID',
  `goods_id`     BIGINT       NOT NULL COMMENT '商品ID',
  `reviewer_id`  BIGINT       NOT NULL COMMENT '评价人ID',
  `target_id`    BIGINT       NOT NULL COMMENT '被评价人ID',
  `role`         TINYINT      NOT NULL COMMENT '角色: 0=买家评卖家, 1=卖家评买家',
  `score`        TINYINT      NOT NULL COMMENT '评分 (1-5)',
  `content`      VARCHAR(500)          DEFAULT NULL COMMENT '评价内容',
  `is_anonymous` TINYINT      NOT NULL DEFAULT 0 COMMENT '是否匿名: 0=否, 1=是',
  `status`       TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 0=隐藏, 1=显示',
  `create_time`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_order` (`order_id`),
  KEY `idx_goods` (`goods_id`),
  KEY `idx_target` (`target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评价表';

-- ============================================
-- 8. 通知表 (简化版系统消息)
-- ============================================
CREATE TABLE IF NOT EXISTS `notice` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `user_id`     BIGINT       NOT NULL COMMENT '接收用户ID',
  `type`        TINYINT      NOT NULL COMMENT '类型: 1=系统通知, 2=订单通知, 3=审核通知',
  `title`       VARCHAR(100) NOT NULL COMMENT '通知标题',
  `content`     VARCHAR(500)          DEFAULT NULL COMMENT '通知内容',
  `is_read`     TINYINT      NOT NULL DEFAULT 0 COMMENT '是否已读: 0=未读, 1=已读',
  `related_id`  BIGINT                DEFAULT NULL COMMENT '关联的订单/商品ID',
  `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_read` (`user_id`, `is_read`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知表';

-- ============================================
-- 插入默认数据
-- ============================================

-- 插入商品分类
INSERT INTO `category` (`name`, `icon`, `sort_order`) VALUES
('教材教辅', '📚', 1),
('电子产品', '💻', 2),
('生活用品', '🏠', 3),
('运动户外', '⚽', 4),
('服饰鞋包', '👗', 5),
('数码配件', '🎧', 6),
('美妆个护', '💄', 7),
('其他', '📦', 99);

-- 插入管理员账号 (密码: admin123, BCrypt加密)
-- 注意: 需要启动项目后通过注册接口创建, 或使用下方默认密码
INSERT INTO `user` (`username`, `password`, `role`, `status`, `credit_score`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5Eh', 1, 1, 1000);
-- 实际BCrypt密文需要在应用中生成, 这里先用占位符
