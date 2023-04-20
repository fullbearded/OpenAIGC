-- ----------------------------
-- Table structure for user_chat
-- ----------------------------
CREATE TABLE `user_chat`
(
	`id`         BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
	`user_id`    BIGINT      NOT NULL COMMENT '用户ID',
	`app_id`     BIGINT      NOT NULL COMMENT 'APP ID',
	`category`   VARCHAR(20) NOT NULL DEFAULT 'PAID' COMMENT '类型: FREE 免费， PAID 付费',
	`token`      INT         NOT NULL DEFAULT 0 COMMENT 'token',
	`questions`  json        NOT NULL COMMENT '问题',
	`answers`    json        NOT NULL COMMENT '答案',
	`created_at` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
	`created_by` VARCHAR(64)          DEFAULT '' COMMENT '创建者',
	INDEX `idx_user_id` (`user_id`) USING BTREE COMMENT '用户ID索引'
) ENGINE = InnoDB
	AUTO_INCREMENT = 1
	DEFAULT CHARSET = utf8mb4 COMMENT ='会员聊天表';
