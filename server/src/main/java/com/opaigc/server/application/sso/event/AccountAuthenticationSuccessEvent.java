package com.opaigc.server.application.sso.event;

import org.springframework.context.ApplicationEvent;

import com.opaigc.server.application.sso.domain.AccountType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: Runner.dada
 * @date: 2020/12/13
 * @description: 用户验证成功事件
 **/
@Getter
@Setter
@ToString
public class AccountAuthenticationSuccessEvent extends ApplicationEvent {

	private AccountType accountType;
	private String username;
	private String token;

	public AccountAuthenticationSuccessEvent(Object source) {
		super(source);
	}
}
