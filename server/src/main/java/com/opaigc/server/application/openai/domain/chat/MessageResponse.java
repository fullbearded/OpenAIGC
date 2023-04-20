package com.opaigc.server.application.openai.domain.chat;

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
public class MessageResponse {
	private MessageType messageType;
	private String message;
	private Boolean done;
}
