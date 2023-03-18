package com.willsoon.willsoon_0_4.entity.Chat;

import com.willsoon.willsoon_0_4.entity.Message.MessagePojo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ChatPojo {
    private UUID chatId;
    private Integer messagesLength;
    private List<MessagePojo> messagePojoList;
}