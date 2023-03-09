package com.willsoon.willsoon_0_4.entity.Message;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;


    public Message save(Message chatMessage) {
        chatMessage.setStatus(MessageStatus.DELIVERED);
        messageRepository.save(chatMessage);
        return chatMessage;
    }

    public Message findById(UUID id) {
        return messageRepository
                .findById(id)
                .map(chatMessage -> {
                    chatMessage.setStatus(MessageStatus.DELIVERED);
                    return messageRepository.save(chatMessage);
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException("can't find message (" + id + ")"));
    }

    public MessagePojo findLastMessageInChat(UUID chatId) {
        Message msg = messageRepository.findLastMessageByChatId(chatId);
        if (msg != null) {
            return new MessagePojo(
                    msg.getText(),
                    msg.getSender().getId(),
                    msg.getSentAt(),
                    msg.getStatus());
        } else {
            return new MessagePojo(
                    "Начните Общение",
                    null,
                    null,
                    null);
        }
    }
}
