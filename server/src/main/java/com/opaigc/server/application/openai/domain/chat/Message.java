package com.opaigc.server.application.openai.domain.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author: Runner.dada
 * @date: 2023/3/23
 * @description:
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {

	/**
	 * 目前支持三种角色参考官网，进行情景输入：https://platform.openai.com/docs/guides/chat/introduction
	 */
	private String role;

	private String content;

	public static Message of(String content) {
		return new Message(Role.USER.getValue(), content);
	}

	public static Message ofSystem(String content) {
		return new Message(Role.SYSTEM.getValue(), content);
	}

	@Getter
	@AllArgsConstructor
	public enum Role {
		SYSTEM("system"),
		USER("user"),
		ASSISTANT("assistant"),
		;
		private final String value;
	}

}
