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

    private UUID messageId;
    private String text;
    private UUID senderId;
    private LocalTime sendAtTime;
    private LocalDate sendAtDate;
    private MessageStatus status;
}
