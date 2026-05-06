-- 修复既有 im_system_message 表缺少当前后台系统消息字段的问题。
-- 执行库：im_platform

ALTER TABLE `im_system_message`
  ADD COLUMN IF NOT EXISTS `cover` VARCHAR(500) DEFAULT '' COMMENT '封面' AFTER `title`,
  ADD COLUMN IF NOT EXISTS `summary` VARCHAR(500) DEFAULT '' COMMENT '简介' AFTER `cover`,
  ADD COLUMN IF NOT EXISTS `content_type` TINYINT NOT NULL DEFAULT 1 COMMENT '内容类型: 1-富文本, 2-外部链接' AFTER `summary`,
  MODIFY COLUMN `content` TEXT DEFAULT NULL COMMENT '富文本内容',
  ADD COLUMN IF NOT EXISTS `link_url` VARCHAR(500) DEFAULT '' COMMENT '外部链接' AFTER `content`,
  MODIFY COLUMN `type` TINYINT NOT NULL DEFAULT 1 COMMENT '类型: 1-系统公告, 2-活动通知, 3-版本更新',
  MODIFY COLUMN `target_type` TINYINT DEFAULT 0 COMMENT '目标类型: 0-全部用户, 1-指定用户',
  ADD COLUMN IF NOT EXISTS `status` TINYINT DEFAULT 0 COMMENT '状态: 0-草稿, 1-已推送' AFTER `target_ids`,
  ADD COLUMN IF NOT EXISTS `creator` BIGINT DEFAULT NULL COMMENT '创建者' AFTER `status`,
  ADD COLUMN IF NOT EXISTS `push_time` DATETIME DEFAULT NULL COMMENT '推送时间' AFTER `creator`,
  MODIFY COLUMN `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  ADD COLUMN IF NOT EXISTS `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' AFTER `created_time`;

CREATE INDEX IF NOT EXISTS `idx_status` ON `im_system_message` (`status`);
CREATE INDEX IF NOT EXISTS `idx_created_time` ON `im_system_message` (`created_time`);
