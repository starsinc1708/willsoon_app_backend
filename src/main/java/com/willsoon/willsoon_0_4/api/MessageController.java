package com.willsoon.willsoon_0_4.api;

import com.willsoon.willsoon_0_4.entity.Message.MessageRepository;
import com.willsoon.willsoon_0_4.entity.Message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;

    private final MessageRepository messageRepository;

    private final MessageService messageService;

}
