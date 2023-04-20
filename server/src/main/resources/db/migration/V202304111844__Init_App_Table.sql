-- ----------------------------
-- Table structure for app
-- ----------------------------
CREATE TABLE `app`
(
	`id`             BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
	`user_id`        BIGINT       NOT NULL COMMENT '用户ID',
	`code`           VARCHAR(32)  NOT NULL DEFAULT '' COMMENT 'APP编码',
	`name`           VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'APP名称',
	`category`       VARCHAR(32)  NOT NULL DEFAULT 'PUBLIC' COMMENT 'APP类型: PUBLIC、PRIVATE',
	`recommend`      VARCHAR(32)  NOT NULL DEFAULT 'NONE' COMMENT '推荐类型: NONE、HOME',
	`upvote`         INT          NOT NULL DEFAULT 0 COMMENT '推荐量',
	`icon`           VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'APP图标',
	`description`    VARCHAR(255) NOT NULL DEFAULT '' COMMENT 'APP描述',
	`used_count`     INT          NOT NULL DEFAULT 0 COMMENT '使用次数',
	`paid_use_count` INT          NOT NULL DEFAULT 0 COMMENT '付费使用次数',
	`forms`          JSON         NOT NULL COMMENT 'APP表单模板',
	`roles`          JSON         NOT NULL COMMENT 'APP预置问题模板',
	`status`         VARCHAR(10)  NOT NULL DEFAULT 'ENABLED' COMMENT '状态: ENABLED 启用， DISABLED 停用',
	`deleted_by`     VARCHAR(64)           DEFAULT '' COMMENT '删除者',
	`deleted_at`     DATETIME              DEFAULT NULL COMMENT '删除时间',
	`created_at`     DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
	`created_by`     VARCHAR(64)           DEFAULT '' COMMENT '创建者',
	`updated_at`     DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
	`updated_by`     VARCHAR(64)           DEFAULT '' COMMENT '更新者',
	INDEX `idx_user_id` (`user_id`) USING BTREE COMMENT '用户ID索引',
	INDEX `idx_code` (`code`) USING BTREE COMMENT 'APP编码索引'
) ENGINE = InnoDB
	AUTO_INCREMENT = 1
	DEFAULT CHARSET = utf8mb4 COMMENT ='会员信息表';

INSERT INTO `app`
VALUES (1, 0, 'a8dpite78tly0nbgacu9vxf2v8th4huu', '默认应用', 'PRIVATE', 'NONE', 0, '', '默认应用', 0, 0, '[]', '[]', 'ENABLED',
				'', NULL,
				'2020-05-06 15:00:00.000', 'admin', '2020-05-06 15:00:00.000', 'admin');
