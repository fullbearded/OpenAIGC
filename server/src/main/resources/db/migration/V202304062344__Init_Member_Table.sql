-- ----------------------------
-- Table structure for member
-- ----------------------------
CREATE TABLE `member`
(
	`id`          BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
	`user_id`     BIGINT      NOT NULL COMMENT '用户ID',
	`expire_date` datetime(3) COMMENT '会员到期日',
	`daily_limit` INT         NOT NULL DEFAULT 0 COMMENT '每日限额',
	`free_used_quota`  INT         NOT NULL DEFAULT 0 COMMENT '免费使用额度统计',
	`used_quota`  INT         NOT NULL DEFAULT 0 COMMENT '已使用额度',
	`total_quota` INT         NOT NULL DEFAULT 0 COMMENT '总查询额度',
	`version`     INT NOT NULL DEFAULT 0 COMMENT '版本号',
	`created_at`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
	`created_by`  VARCHAR(64)          DEFAULT '' COMMENT '创建者',
	`updated_at`  datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
	`updated_by`  VARCHAR(64)          DEFAULT '' COMMENT '更新者',
	UNIQUE KEY `users_user_code_udx` (`user_id`)
) ENGINE = InnoDB
	AUTO_INCREMENT = 1
	DEFAULT CHARSET = utf8mb4 COMMENT ='会员信息表';
