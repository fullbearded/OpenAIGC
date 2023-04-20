package com.opaigc.server.application.openai.domain.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import lombok.Data;

/**
 * @author: Runner.dada
 * @date: 2023/3/23
 * @description: ChatChoice
 **/
@Data
public class ChatChoice implements Serializable {

	private long index;
	/**
	 * 请求参数stream为true返回是delta
	 */
	@JsonProperty("delta")
	private Message delta;

	/**
	 * 请求参数stream为false返回是message
	 */
	@JsonProperty("message")
	private Message message;

	@JsonProperty("finish_reason")
	private String finishReason;
}
