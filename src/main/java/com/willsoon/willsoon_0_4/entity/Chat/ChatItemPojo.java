package com.willsoon.willsoon_0_4.entity.Chat;

import com.willsoon.willsoon_0_4.entity.Message.MessagePojo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ChatItemPojo {
    private UUID chatId;
    private UUID recipientId;
    private String recipientUsername;
    private Boolean online;
    private MessagePojo lastMessage;
}
