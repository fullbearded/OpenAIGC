package com.opaigc.server.openai.listener;


import com.opaigc.server.openai.domain.chat.Message;
import com.opaigc.server.openai.domain.chat.MessageQuestion;

/**
 * @author: Runner.dada
 * @date: 2023/3/28
 * @description:
**/
public interface CompletedCallBack {

    /**
     * 完成回掉
     *
     * @param questions
     * @param sessionId
     * @param response
     */
    void completed(MessageQuestion questions, String sessionId, String response);

    void fail(String sessionId);

}
