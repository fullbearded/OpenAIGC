-- ----------------------------
-- Table structure for app
-- ----------------------------
ALTER TABLE `app` ADD COLUMN `ext` json DEFAULT NULL COMMENT '扩展字段' AFTER `status`;