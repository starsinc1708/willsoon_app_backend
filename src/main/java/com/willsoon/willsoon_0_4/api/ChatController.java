package com.willsoon.willsoon_0_4.api;

import com.willsoon.willsoon_0_4.entity.AppUser.AppUser;
import com.willsoon.willsoon_0_4.entity.AppUser.AppUserRepository;
import com.willsoon.willsoon_0_4.entity.Chat.*;
import com.willsoon.willsoon_0_4.entity.Message.Message;
import com.willsoon.willsoon_0_4.entity.Message.MessagePojo;
import com.willsoon.willsoon_0_4.entity.Message.MessageService;
import com.willsoon.willsoon_0_4.security.config.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;
    private final AppUserRepository userRepository;
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final JwtService jwtService;
    public String extractEmailFromToken(@NonNull HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            throw new RuntimeException("Refresh Token is Missing");
        }
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        return userEmail;
    }

    @GetMapping("/userChats")
    public ResponseEntity<List<ChatItemPojo>> getAllUserChats(@NonNull HttpServletRequest request) {
        AppUser curUser = userRepository.findByEmail(extractEmailFromToken(request)).get();
        return ResponseEntity.ok().body(chatService.getUserChats(curUser.getId()));
    }

    @GetMapping("/")
    public ResponseEntity<ChatPojo> getChatById(@RequestParam("id") String chatId, @RequestParam("offset") String offset) {
        Chat chat = chatService.getChatById(UUID.fromString(chatId));
        List<MessagePojo> messagePojoList = messageService.findMessagesInChat(UUID.fromString(chatId), offset);
        return ResponseEntity.ok().body(
                new ChatPojo(
                        chat.getId(),
                        messagePojoList
                )
        );
    }

    @MessageMapping("/chat")
    public void processMessage(@Payload Message chatMessage) {

        Chat chat = chatService.getChatById(chatMessage.getSender().getId());
        chatMessage.setChat(chat);

        Message saved = messageService.save(chatMessage);

        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipient().getId().toString(),
                "/queue/messages",
                new ChatNotification(
                        saved.getId().toString(),
                        saved.getSender().getId().toString(),
                        saved.getSender().getDBUsername()
                )
        );
    }
}
