-- ----------------------------
-- Table structure for promotion
-- ----------------------------
CREATE TABLE `promotion`
(
	`id`          BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
	`name`        VARCHAR(256)  NOT NULL DEFAULT '' COMMENT '促销名称',
	`code`        VARCHAR(16)   NOT NULL DEFAULT '' COMMENT '促销码',
	`status`      VARCHAR(32)   NOT NULL DEFAULT 'UNAVAILABLE' COMMENT '促销状态，UNAVAILABLE 不可用，AVAILABLE 可用',
	`type`        VARCHAR(32)   NOT NULL DEFAULT 'UNKNOWN' COMMENT '码类型, TIME 次数卡，MONTH 月卡',
	`rule`        VARCHAR(2048) NULL COMMENT '规则',

	`usage_limit` INT(10)       NOT NULL DEFAULT 0 COMMENT '使用次数',
	`start_at`    DATETIME(3)   NULL COMMENT '可用开始时间',
	`end_at`      DATETIME(3)   NULL COMMENT '可用结束时间',

	`created_at`  datetime(3)   NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
	`created_by`  VARCHAR(64)            DEFAULT '' COMMENT '创建者',
	`updated_at`  datetime(3)   NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
	`updated_by`  VARCHAR(64)            DEFAULT '' COMMENT '更新者'
) ENGINE = InnoDB
	AUTO_INCREMENT = 1
	DEFAULT CHARSET = utf8mb4 COMMENT ='促销码表';
