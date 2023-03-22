package com.willsoon.willsoon_0_4.entity.Message;

import com.willsoon.willsoon_0_4.entity.Chat.ChatItemPojo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
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
                    msg.getChat().getId().toString(),
                    msg.getId().toString(),
                    msg.getSender().getId().toString(),
                    msg.getRecipient().getId().toString(),
                    msg.getText(),
                    msg.getSentAt().toLocalTime().toString(),
                    msg.getSentAt().toLocalDate().toString(),
                    msg.getStatus().toString());
        } else {
            return new MessagePojo(
                    null,
                    null,
                    null,
                    null,
                    "Начните общение",
                    null,
                    null,
                    null);
        }
    }
    public List<MessagePojo> findMessagesInChat(UUID chatId, Integer offset) {
        List<Message> messageList = messageRepository.findMessagesByChatIdAndSentAtWithOffset(chatId, offset);
        return messageList
                .stream()
                .map(message -> {
                    return new MessagePojo(
                            message.getChat().getId().toString(),
                            message.getId().toString(),
                            message.getSender().getId().toString(),
                            message.getRecipient().getId().toString(),
                            message.getText(),
                            message.getSentAt().toLocalTime().toString(),
                            message.getSentAt().toLocalDate().toString(),
                            message.getStatus().toString());
                })
                .toList();
    }
}
