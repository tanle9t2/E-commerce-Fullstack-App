package com.tanle.e_commerce.service;

import com.tanle.e_commerce.dto.ChatMessageDTO;
import com.tanle.e_commerce.entities.ChatMessage;

import java.util.List;

public interface ChatMessageService {
    List<ChatMessageDTO> getChatMessageByRoom(int senderId, int recipientId);
    ChatMessageDTO saveMessage(ChatMessage chatMessage,int senderId,int recipientId);
}
