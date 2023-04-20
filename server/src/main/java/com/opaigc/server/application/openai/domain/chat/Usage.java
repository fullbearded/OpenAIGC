package com.opaigc.server.application.openai.domain.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import lombok.Data;

/**
 * @author: Runner.dada
 * @date: 2023/3/23
 * @description:
 **/
@Data
public class Usage implements Serializable {
	@JsonProperty("prompt_tokens")
	private long promptTokens;
	@JsonProperty("completion_tokens")
	private long completionTokens;
	@JsonProperty("total_tokens")
	private long totalTokens;
}
