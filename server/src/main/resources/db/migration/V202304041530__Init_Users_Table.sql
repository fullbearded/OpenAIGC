-- ----------------------------
-- Table structure for users
-- ----------------------------
CREATE TABLE `users`
(
	`id`              BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
	`code`            VARCHAR(32)  NOT NULL DEFAULT '' COMMENT '用户编码',
	`username`        VARCHAR(100) NOT NULL DEFAULT '' COMMENT '用户名',
	`email`           VARCHAR(50)  NOT NULL DEFAULT '' COMMENT '用户邮箱',
	`mobile`          VARCHAR(11)  NOT NULL DEFAULT '' COMMENT '手机号码',
	`password`        VARCHAR(100)          DEFAULT '' COMMENT '密码',
	`avatar`          VARCHAR(100)          DEFAULT '' COMMENT '头像地址',
	`sex`             VARCHAR(10)           DEFAULT 'MAN' COMMENT '用户性别: MAN男 WOMAN女 UNKNOWN未知',
	`user_type`       VARCHAR(20)           DEFAULT 'SYSTEM' COMMENT '用户类型: SYSTEM 系统用户 WECOM 企业微信用户 USER 普通用户',
	`status`          VARCHAR(20)           DEFAULT 'ENABLE' COMMENT '帐号状态：ENABLE 正常使用 BANNED 禁用',
	`register_ip`     VARCHAR(50)           DEFAULT '' COMMENT '注册IP',
	`last_login_ip`   VARCHAR(50)           DEFAULT '' COMMENT '最后登陆IP',
	`last_login_date` DATETIME              DEFAULT NULL COMMENT '最后登陆时间',
	`remark`          VARCHAR(500)          DEFAULT '' COMMENT '备注信息',
	`deleted_by`      VARCHAR(64)           DEFAULT '' COMMENT '删除者',
	`deleted_at`      DATETIME              DEFAULT NULL COMMENT '删除时间',
	`created_at`      datetime(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
	`created_by`      VARCHAR(64)           DEFAULT '' COMMENT '创建者',
	`updated_at`      datetime(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
	`updated_by`      VARCHAR(64)           DEFAULT '' COMMENT '更新者',
	UNIQUE KEY `users_user_code_udx` (`code`),
	UNIQUE KEY `users_mobile_udx` (`mobile`)
) ENGINE = InnoDB
	AUTO_INCREMENT = 1
	DEFAULT CHARSET = utf8mb4 COMMENT ='用户信息表';

INSERT INTO `users`(id, code, username, email, mobile, password, avatar,
										sex, user_type, status, last_login_ip, last_login_date, remark,
										deleted_by, deleted_at, created_by, created_at, updated_at, updated_by)
VALUES (1, '7c8aea9d04e70b3f3df28815f6e8a2ab', 'admin', 'admin@liankebang.com', '18888888888',
				'$2a$10$acM70fMVPceNyIHkg1h/he6QLHJ1XZHDmLtVR8RQFglB2WAJKmAm2', 'avatar', 'MAN', 'SYSTEM',
				'ENABLE', '127.0.0.1', '2020-12-17 13:34:36', '超级管理员，拥有所有的权限', NULL, NULL, 'system',
				'2020-12-17 13:34:36', '2020-12-17 13:34:36', 'system'),
			 (2, '7c8aea9d04e70b3f3df28815f6e8a2aa', 'jerry', 'jerry@liankebang.com', '18888888881',
				'$2a$10$acM70fMVPceNyIHkg1h/he6QLHJ1XZHDmLtVR8RQFglB2WAJKmAm2', 'avatar', 'MAN', 'USER',
				'ENABLE', '127.0.0.1', '2020-12-17 13:34:36', '普通用户，拥有所有的权限', NULL, NULL, 'system',
				'2020-12-17 13:34:36', '2020-12-17 13:34:36', 'system')
;

