package com.willsoon.willsoon_0_4.entity.Message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class MessagePojo {
    private String chatId;
    private String messageId;
    private String senderId;
    private String recipientId;
    private String text;
    private String sendAtTime;
    private String sendAtDate;
    private MessageStatus status;
}
