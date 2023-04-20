package com.opaigc.server.application.openai.domain.chat;

import com.opaigc.server.application.openai.service.OpenAiService;
import com.opaigc.server.application.user.domain.UserChat;
import com.opaigc.server.infrastructure.common.Constants;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述
 *
 * @author huhongda@fiture.com
 * @date 2023/3/28
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageQuestion {
	private MessageType messageType;
	private String message;
	private Date date;
	private String remoteIp;
	private List<OpenAiService.Message> messages;
	private UserChat.ChatCategoryEnum chatType;
	private Long appId;

	public MessageQuestion(MessageType messageType, List<OpenAiService.Message> messages,
												 String remoteIp, UserChat.ChatCategoryEnum chatType,
												 Long appId) {
		this.appId = Optional.ofNullable(appId).orElse(Constants.DEFAULT_APP_ID);
		this.messageType = messageType;
		this.messages = messages;
		this.remoteIp = remoteIp;
		this.chatType = chatType;
		this.date = new Date();
	}
}
