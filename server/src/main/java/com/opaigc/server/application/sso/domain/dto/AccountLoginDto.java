package com.opaigc.server.application.sso.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @author: Runner.dada
 * @date: 2020/12/13
 * @description: 系统用户登录DTO
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class AccountLoginDto extends LoginUserDto {
	private String role;
}
