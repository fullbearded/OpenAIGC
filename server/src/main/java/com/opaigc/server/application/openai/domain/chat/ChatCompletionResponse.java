package com.opaigc.server.application.openai.domain.chat;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * @author: Runner.dada
 * @date: 2023/3/23
 * @description:
 **/
@Data
public class ChatCompletionResponse implements Serializable {

	private String id;

	private String object;

	private long created;

	private String model;

	private List<ChatChoice> choices;

	private Usage usage;
}
