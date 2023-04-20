package com.opaigc.server.application.user.event;

import com.opaigc.server.application.openai.domain.chat.MessageQuestion;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: Runner.dada
 * @date: 2020/12/13
 * @description: 用户验证失败事件
 **/
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatStreamCompletedEvent implements Serializable {

	private String sessionId;

	private String response;

	private MessageQuestion questions;
}
