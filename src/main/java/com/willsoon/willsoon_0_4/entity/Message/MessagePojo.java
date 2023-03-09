package com.willsoon.willsoon_0_4.entity.Message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class MessagePojo {

    private String text;
    private UUID senderId;
    private LocalDateTime sendAt;
    private MessageStatus status;
}
