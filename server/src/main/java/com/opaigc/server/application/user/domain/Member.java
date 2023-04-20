package com.opaigc.server.application.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 描述
 *
 * @author huhongda@fiture.com
 * @date 2023/4/6
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "member", autoResultMap = true)
public class Member {
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 用户id
	 **/
	private Long userId;
	/**
	 * 会员到期日
	 **/
	private LocalDateTime expireDate;
	/**
	 * 每日限额
	 **/
	private Integer dailyLimit;
	/**
	 * 已使用额度-免费
	 **/
	private Integer freeUsedQuota;
	/**
	 * 已使用额度
	 **/
	private Integer usedQuota;
	/**
	 * 总查询额度
	 **/
	private Integer totalQuota;

	/**
	 * 乐观锁版本号
	 **/
	@Version
	@TableField("version")
	private Integer version;

	/**
	 * 创建时间
	 */
	private LocalDateTime createdAt;
	/**
	 * 创建人
	 **/
	private String createdBy;
	/**
	 * 修改时间
	 */
	private LocalDateTime updatedAt;
	/**
	 * 更新人
	 **/
	private String updatedBy;

	public Boolean isExpired() {
		return expireDate.isBefore(LocalDateTime.now());
	}

	public Boolean isFreeUser() {
		return totalQuota == 0 || isExpired();
	}

	public Boolean isVip() {
		return !isFreeUser();
	}

}
