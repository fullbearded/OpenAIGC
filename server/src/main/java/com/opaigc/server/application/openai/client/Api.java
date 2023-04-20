package com.opaigc.server.application.openai.client;

/**
 * @author: Runner.dada
 * @date: 2023/3/23
 * @description:
 **/
public interface Api {

	String CHAT_PATH = "/v1/chat/completions";

	String CONTENT_AUDIT_PATH = "/v1/moderations";

	String CREDIT_GRANTS_PATH = "/dashboard/billing/credit_grants";

}
