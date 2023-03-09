package com.willsoon.willsoon_0_4.api;

import com.willsoon.willsoon_0_4.entity.AppUser.AppUser;
import com.willsoon.willsoon_0_4.entity.AppUser.AppUserRepository;
import com.willsoon.willsoon_0_4.entity.Chat.Chat;
import com.willsoon.willsoon_0_4.entity.Chat.ChatNotification;
import com.willsoon.willsoon_0_4.entity.Chat.ChatPojo;
import com.willsoon.willsoon_0_4.entity.Chat.ChatService;
import com.willsoon.willsoon_0_4.entity.Message.Message;
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

    /*@GetMapping("/")
    public ResponseEntity<ChatPojo> getChatById(@RequestParam("id") String id) {
        Chat chat = chatService.getChatById(UUID.fromString(id));



        ChatPojo chatPojo = new ChatPojo(
                chat.getSender().getDBUsername(),
                chat.getRecipient().getDBUsername(),
                messageService.findLastMessageInChat(chat.getId()));
        return ResponseEntity.ok().body(chatPojo);
    }*/

    @GetMapping("/userChats")
    public ResponseEntity<List<ChatPojo>> getAllUserChats(@NonNull HttpServletRequest request) {
        AppUser curUser = userRepository.findByEmail(extractEmailFromToken(request)).get();
        return ResponseEntity.ok().body(chatService.getUserChats(curUser.getId()));
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
