package com.willsoon.willsoon_0_4.entity.Chat;

import com.willsoon.willsoon_0_4.entity.Message.MessagePojo;
import com.willsoon.willsoon_0_4.entity.Message.MessageRepository;
import com.willsoon.willsoon_0_4.entity.Message.MessageService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ChatService {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private MessageService messageService;
    public Chat getChatById(UUID uuid) {
        return chatRepository.findAllById(uuid)
                .orElseThrow();
    }

    public UUID getChatId(UUID uuid) {
        return chatRepository.findById(uuid).get().getId();
    }

    public List<ChatItemPojo> getUserChats(UUID userId) {

        List<Chat> chats = chatRepository.findAllBySenderOrRecipient(userId);

        return chats
                .stream()
                .map(chat -> {
                    UUID recipientId = userId == chat.getSender().getId() ? chat.getRecipient().getId() : chat.getSender().getId();
                    String recipientUsername = userId == chat.getSender().getId() ? chat.getRecipient().getDBUsername() : chat.getSender().getDBUsername();
                    MessagePojo lastMessage = messageService.findLastMessageInChat(chat.getId());
                    Boolean online = userId == chat.getSender().getId() ? chat.getRecipient().getActive() : chat.getSender().getActive();
                    return new ChatItemPojo(chat.getId(), recipientId, recipientUsername, online, lastMessage);
                })
                .toList();
    }

    public ChatItemPojo getChatByRecipientId(UUID recipientId, UUID userId) {
        Chat chat = chatRepository.findBySenderAndRecipient(recipientId, userId);
        String recipientUsername = userId == chat.getSender().getId() ? chat.getRecipient().getDBUsername() : chat.getSender().getDBUsername();
        MessagePojo lastMessage = messageService.findLastMessageInChat(chat.getId());
        Boolean online = userId == chat.getSender().getId() ? chat.getRecipient().getActive() : chat.getSender().getActive();
        return new ChatItemPojo(
                chat.getId(),
                recipientId,
                recipientUsername,
                online,
                lastMessage
        );
    }
}
