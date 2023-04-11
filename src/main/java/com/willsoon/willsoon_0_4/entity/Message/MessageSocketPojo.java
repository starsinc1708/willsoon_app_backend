package com.willsoon.willsoon_0_4.entity.Message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageSocketPojo {
    private String messageId;
    private String chatId;
    private String senderId;
    private String recipientId;
    private String text;
    private String sendAtTime;
}
