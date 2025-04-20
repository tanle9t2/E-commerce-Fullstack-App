package com.tanle.e_commerce.controller;

import com.tanle.e_commerce.dto.ChatMessageDTO;
import com.tanle.e_commerce.entities.ChatMessage;
import com.tanle.e_commerce.entities.ChatNotification;
import com.tanle.e_commerce.entities.MyUser;
import com.tanle.e_commerce.service.ChatMessageService;
import com.tanle.e_commerce.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:5173")
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private FirebaseService firebaseService;

    @MessageMapping("/chat/{sender}/{receiver}")
    public void processingMessage(@Payload ChatMessage chatMessage,
                                  @DestinationVariable String sender,
                                  @DestinationVariable String receiver) {
        ChatMessage svChat = firebaseService.saveMessage(chatMessage, Integer.parseInt(sender)
                , Integer.parseInt(receiver));
        messagingTemplate.convertAndSendToUser(receiver, "/queue/messages",
                ChatNotification.builder()
                        .id(svChat.getId())
                        .senderId(Integer.parseInt(sender))
                        .timestamp(svChat.getTimestamp())
                        .recipientId(Integer.parseInt(receiver))
                        .content(svChat.getContent())
                        .build());
    }

    @GetMapping("/messages/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@AuthenticationPrincipal MyUser user,
                                                              @PathVariable int recipientId) {
        List<ChatMessage> chatMessages = firebaseService.getChatMessageByRoom(user.getId(), recipientId);
        return ResponseEntity.ok(chatMessages);
    }
}
