-- =====================================================
-- HAUST Exchange Platform Database Schema
-- =====================================================
-- Description: Database initialization script for HAUST Exchange Platform
-- Author: Auto-generated from entity classes
-- Date: 2025-10-23
-- =====================================================

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS `haust_exchange_platform` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `haust_exchange_platform`;

-- =====================================================
-- 1. User Management Tables
-- =====================================================

-- User table: stores user account information
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `account` VARCHAR(100) NOT NULL COMMENT '用户账号',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（加密）',
    `role` TINYINT DEFAULT 1 COMMENT '角色：0=管理员，1=普通用户，2=问题账户',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_account` (`account`),
    KEY `idx_role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- User monitor table: tracks user login behavior
DROP TABLE IF EXISTS `user_monitor`;
CREATE TABLE `user_monitor` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `account` VARCHAR(100) NOT NULL COMMENT '用户账号',
    `login_times` INT DEFAULT 0 COMMENT '登录次数',
    `ip` VARCHAR(50) COMMENT 'IP地址',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_account` (`account`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户监控表';

-- =====================================================
-- 2. Referral Management Tables
-- =====================================================

-- Coding sharing table: stores internal referral information
DROP TABLE IF EXISTS `coding_sharing`;
CREATE TABLE `coding_sharing` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `company_name` VARCHAR(200) NOT NULL COMMENT '公司名称',
    `detail` TEXT COMMENT '详细内推信息',
    `remark` TEXT COMMENT '备注信息',
    `recommander_email` VARCHAR(255) COMMENT '推荐者邮箱地址',
    `click_number` INT DEFAULT 0 COMMENT '浏览次数',
    `recommand_index` TINYINT DEFAULT 0 COMMENT '推荐指数（1-5）',
    `status` TINYINT DEFAULT 0 COMMENT '状态：0=未审核，1=已通过，-1=未通过',
    `code_id` VARCHAR(100) COMMENT '内推码',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`),
    KEY `idx_click_number` (`click_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='内推信息表';

-- =====================================================
-- 3. Forum Management Tables
-- =====================================================

-- Post table: stores forum posts
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键，帖子ID',
    `title` VARCHAR(255) NOT NULL COMMENT '帖子标题',
    `description` TEXT COMMENT '帖子详细信息',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `latest_answer_id` BIGINT COMMENT '最新回答ID',
    `answer_times` INT DEFAULT 0 COMMENT '回答数量',
    `anonymity` BOOLEAN DEFAULT TRUE COMMENT '是否匿名',
    `click_count` BIGINT DEFAULT 0 COMMENT '浏览次数',
    `liked_times` INT DEFAULT 0 COMMENT '点赞数量',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '提问时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_time` (`create_time`),
    KEY `idx_liked_times` (`liked_times`),
    KEY `idx_click_count` (`click_count`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='帖子表';

-- Post reply table: stores post comments and replies
DROP TABLE IF EXISTS `post_reply`;
CREATE TABLE `post_reply` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '帖子回答ID',
    `post_id` BIGINT NOT NULL COMMENT '帖子ID',
    `answer_id` BIGINT COMMENT '回复的上级回答ID',
    `user_id` BIGINT NOT NULL COMMENT '回答者ID',
    `content` TEXT NOT NULL COMMENT '回答内容',
    `target_user_id` BIGINT COMMENT '回复的目标用户ID',
    `target_reply_id` BIGINT COMMENT '回复的目标回复ID',
    `reply_times` INT DEFAULT 0 COMMENT '评论数量',
    `liked_times` INT DEFAULT 0 COMMENT '点赞数量',
    `anonymity` BOOLEAN DEFAULT TRUE COMMENT '是否匿名',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_tsime` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_post_id` (`post_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_target_reply_id` (`target_reply_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='帖子评论表';

-- =====================================================
-- 4. Initial Data
-- =====================================================

-- Insert default admin user (password: admin123)
-- Note: Password should be encrypted using BCrypt in actual application
INSERT INTO `user` (`account`, `password`, `role`) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 0);

-- Note: The password 'admin123' is hashed with BCrypt
-- Use this account to login as admin for the first time
-- Remember to change the password after first login

-- =====================================================
-- Database Schema Creation Completed
-- =====================================================
