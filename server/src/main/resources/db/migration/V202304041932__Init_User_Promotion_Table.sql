-- ----------------------------
-- Table structure for user_promotion
-- ----------------------------
CREATE TABLE `user_promotion`
(
	`id`             BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',

	`user_id`        BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用的用户id',
	`scene`          VARCHAR(32)     NOT NULL DEFAULT '' COMMENT '业务场景',
	`promotion_id`   BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '兑换码的id',
	`promotion_code` VARCHAR(16)     NOT NULL DEFAULT '' COMMENT '兑换码编码',
	`promotion_name` VARCHAR(256)    NOT NULL DEFAULT '' COMMENT '兑换码名称',

	`status`         VARCHAR(32)     NOT NULL DEFAULT 'LOCKED' COMMENT '状态，LOCKED 已锁定，USED 已核销，REVOKED 已作废',
	`charge_data`    JSON            NULL COMMENT '锁定数据',
	`charge_at`      DATETIME(3)     NULL COMMENT '核销时间',
	`revoke_at`      DATETIME(3)     NULL COMMENT '作废时间',

	`created_at`     datetime(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
	`created_by`     VARCHAR(64)              DEFAULT '' COMMENT '创建者',
	`updated_at`     datetime(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
	`updated_by`     VARCHAR(64)              DEFAULT '' COMMENT '更新者'
) ENGINE = InnoDB
	AUTO_INCREMENT = 1
	DEFAULT CHARSET = utf8mb4 COMMENT ='促销使用记录表';
