package com.willsoon.willsoon_0_4.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.willsoon.willsoon_0_4.entity.AppUser.AppUser;
import com.willsoon.willsoon_0_4.entity.AppUser.AppUserRepository;
import com.willsoon.willsoon_0_4.entity.Chat.*;
import com.willsoon.willsoon_0_4.entity.Message.*;
import com.willsoon.willsoon_0_4.security.config.ErrorResponse;
import com.willsoon.willsoon_0_4.security.config.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    public ResponseEntity<?> getAllUserChats(@NonNull HttpServletRequest request) {
        AppUser curUser = userRepository.findByEmail(extractEmailFromToken(request)).get();
        try {
            List<ChatItemPojo> chatItemPojos = chatService.getUserChats(curUser.getId());
            return ResponseEntity.ok().body(chatItemPojos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Wrong Chat Id"));
        }

    }

    @GetMapping("/")
    public ResponseEntity<?> getChatById(@RequestParam("id") String chatId, @RequestParam("offset") String offset) {

        try {
            Chat chat = chatService.getChatById(UUID.fromString(chatId));
            List<MessagePojo> messagePojoList = messageService.findMessagesInChat(UUID.fromString(chatId), Integer.parseInt(offset));
            return ResponseEntity.ok().body(
                    new ChatPojo(
                            chat.getId(),
                            messagePojoList.size(),
                            messagePojoList
                    )
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Wrong Chat Id"));
        }
    }

    @MessageMapping("/chat")
    public void processMessage(@Payload MessagePojo messagePojo) {

        Message message = new Message(
                messagePojo.getText(),
                chatService.getChatById(UUID.fromString(messagePojo.getChatId())),
                userRepository.findById(UUID.fromString(messagePojo.getSenderId())).orElseThrow(() -> new UsernameNotFoundException("Sender not found")),
                userRepository.findById(UUID.fromString(messagePojo.getRecipientId())).orElseThrow(() -> new UsernameNotFoundException("Recipient not found")),
                LocalDateTime.now(),
                MessageStatus.DELIVERED
        );

        Message saved = messageService.save(message);

        messagingTemplate.convertAndSendToUser(
                message.getRecipient().getId().toString(),
                "/queue/messages",
                messagePojo
        );
    }
}
