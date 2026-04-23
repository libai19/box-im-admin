-- 商业版后台补充能力：系统消息、推送任务、用户投诉、群状态
-- 执行库：im_platform（业务表）与 im_admin（菜单权限）分别执行对应段落。

-- ===== 在 im_platform 执行 =====
CREATE TABLE IF NOT EXISTS `im_system_message` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` VARCHAR(100) NOT NULL COMMENT '标题',
  `cover` VARCHAR(500) DEFAULT '' COMMENT '封面',
  `summary` VARCHAR(500) DEFAULT '' COMMENT '简介',
  `content_type` TINYINT NOT NULL DEFAULT 1 COMMENT '内容类型: 1-富文本, 2-外部链接',
  `content` TEXT DEFAULT NULL COMMENT '富文本内容',
  `link_url` VARCHAR(500) DEFAULT '' COMMENT '外部链接',
  `type` TINYINT NOT NULL DEFAULT 1 COMMENT '类型: 1-系统公告, 2-活动通知, 3-版本更新',
  `target_type` TINYINT DEFAULT 0 COMMENT '目标类型: 0-全部用户, 1-指定用户',
  `target_ids` TEXT DEFAULT NULL COMMENT '目标用户ID，逗号分隔',
  `status` TINYINT DEFAULT 0 COMMENT '状态: 0-草稿, 1-已推送',
  `creator` BIGINT DEFAULT NULL COMMENT '创建者',
  `push_time` DATETIME DEFAULT NULL COMMENT '推送时间',
  `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_created_time` (`created_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统消息';

CREATE TABLE IF NOT EXISTS `im_push_task` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `message_id` BIGINT DEFAULT NULL COMMENT '系统消息ID',
  `title` VARCHAR(100) NOT NULL COMMENT '推送标题',
  `content` TEXT DEFAULT NULL COMMENT '推送内容',
  `target_type` TINYINT DEFAULT 0 COMMENT '目标类型: 0-全部用户, 1-指定用户',
  `target_ids` TEXT DEFAULT NULL COMMENT '目标用户ID，逗号分隔',
  `status` TINYINT DEFAULT 0 COMMENT '推送状态: 0-待发送, 1-已发送, 2-失败',
  `fail_reason` VARCHAR(500) DEFAULT NULL COMMENT '失败原因',
  `creator` BIGINT DEFAULT NULL COMMENT '创建者',
  `push_time` DATETIME DEFAULT NULL COMMENT '推送时间',
  `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_message_id` (`message_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='推送任务';

CREATE TABLE IF NOT EXISTS `im_complaint` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT NOT NULL COMMENT '投诉人ID',
  `target_id` BIGINT NOT NULL COMMENT '投诉对象ID',
  `target_type` TINYINT NOT NULL DEFAULT 1 COMMENT '投诉对象类型: 1-用户, 2-群',
  `type` TINYINT NOT NULL COMMENT '投诉原因: 1-垃圾信息, 2-骚扰, 3-诈骗, 4-其他',
  `content` VARCHAR(500) NOT NULL COMMENT '投诉内容',
  `images` VARCHAR(1000) DEFAULT NULL COMMENT '截图，逗号分隔',
  `status` TINYINT DEFAULT 0 COMMENT '状态: 0-未处理, 1-处理中, 2-已处理',
  `result` VARCHAR(500) DEFAULT NULL COMMENT '处理结果',
  `handle_by` BIGINT DEFAULT NULL COMMENT '处理人',
  `handle_time` DATETIME DEFAULT NULL COMMENT '处理时间',
  `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_target` (`target_type`, `target_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户投诉';

ALTER TABLE `im_group` ADD COLUMN IF NOT EXISTS `muted` TINYINT(1) DEFAULT 0 COMMENT '是否开启全员禁言' AFTER `is_banned`;

-- ===== 在 im_admin 执行 =====
INSERT IGNORE INTO sys_menu VALUES('62', '推送任务', '6', '3', 'pushTask', 'im/pushTask/index', '', 1, 0, 'C', '0', '0', 'im:pushTask:list', 'message', 103, 1, SYSDATE(), NULL, NULL, 'IM推送任务');
INSERT IGNORE INTO sys_menu VALUES('6201', '推送任务查询', '62', '1', '#', '', '', 1, 0, 'F', '0', '0', 'im:pushTask:query', '#', 103, 1, SYSDATE(), NULL, NULL, '');
INSERT IGNORE INTO sys_menu VALUES('6202', '推送任务删除', '62', '2', '#', '', '', 1, 0, 'F', '0', '0', 'im:pushTask:remove', '#', 103, 1, SYSDATE(), NULL, NULL, '');
INSERT IGNORE INTO sys_menu VALUES('6203', '再次推送', '62', '3', '#', '', '', 1, 0, 'F', '0', '0', 'im:pushTask:resend', '#', 103, 1, SYSDATE(), NULL, NULL, '');
INSERT IGNORE INTO sys_menu VALUES('63', '系统消息', '6', '4', 'systemMessage', 'im/systemMessage/index', '', 1, 0, 'C', '0', '0', 'im:systemMessage:list', 'message', 103, 1, SYSDATE(), NULL, NULL, 'IM系统消息');
INSERT IGNORE INTO sys_menu VALUES('6301', '系统消息查询', '63', '1', '#', '', '', 1, 0, 'F', '0', '0', 'im:systemMessage:query', '#', 103, 1, SYSDATE(), NULL, NULL, '');
INSERT IGNORE INTO sys_menu VALUES('6302', '系统消息新增', '63', '2', '#', '', '', 1, 0, 'F', '0', '0', 'im:systemMessage:add', '#', 103, 1, SYSDATE(), NULL, NULL, '');
INSERT IGNORE INTO sys_menu VALUES('6303', '系统消息修改', '63', '3', '#', '', '', 1, 0, 'F', '0', '0', 'im:systemMessage:edit', '#', 103, 1, SYSDATE(), NULL, NULL, '');
INSERT IGNORE INTO sys_menu VALUES('6304', '系统消息删除', '63', '4', '#', '', '', 1, 0, 'F', '0', '0', 'im:systemMessage:remove', '#', 103, 1, SYSDATE(), NULL, NULL, '');
INSERT IGNORE INTO sys_menu VALUES('6305', '系统消息推送', '63', '5', '#', '', '', 1, 0, 'F', '0', '0', 'im:systemMessage:push', '#', 103, 1, SYSDATE(), NULL, NULL, '');
INSERT IGNORE INTO sys_menu VALUES('6306', '系统消息导出', '63', '6', '#', '', '', 1, 0, 'F', '0', '0', 'im:systemMessage:export', '#', 103, 1, SYSDATE(), NULL, NULL, '');
INSERT IGNORE INTO sys_menu VALUES('8', '用户投诉', '0', '4', 'im/complaint', 'im/complaint/index', '', 1, 0, 'C', '0', '0', 'im:complaint:list', 'documentation', 103, 1, SYSDATE(), NULL, NULL, 'IM用户投诉');
INSERT IGNORE INTO sys_menu VALUES('8001', '投诉查询', '8', '1', '#', '', '', 1, 0, 'F', '0', '0', 'im:complaint:query', '#', 103, 1, SYSDATE(), NULL, NULL, '');
INSERT IGNORE INTO sys_menu VALUES('8002', '投诉处理', '8', '2', '#', '', '', 1, 0, 'F', '0', '0', 'im:complaint:handle', '#', 103, 1, SYSDATE(), NULL, NULL, '');
INSERT IGNORE INTO sys_menu VALUES('8003', '投诉删除', '8', '3', '#', '', '', 1, 0, 'F', '0', '0', 'im:complaint:remove', '#', 103, 1, SYSDATE(), NULL, NULL, '');
INSERT IGNORE INTO sys_menu VALUES('8004', '投诉导出', '8', '4', '#', '', '', 1, 0, 'F', '0', '0', 'im:complaint:export', '#', 103, 1, SYSDATE(), NULL, NULL, '');
INSERT IGNORE INTO sys_menu VALUES('5005', '群聊状态', '5', '5', '#', '', '', 1, 0, 'F', '0', '0', 'im:group:status', '#', 103, 1, SYSDATE(), NULL, NULL, '');
