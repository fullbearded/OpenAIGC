package com.opaigc.server.openai.domain.chat;

import java.util.Date;
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

    public MessageQuestion(MessageType messageType, String message) {
        this.messageType = messageType;
        this.message = message;
        this.date = new Date();
    }
}
