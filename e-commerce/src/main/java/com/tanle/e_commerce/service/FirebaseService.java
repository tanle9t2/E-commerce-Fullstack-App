package com.tanle.e_commerce.service;

import com.tanle.e_commerce.dto.ChatMessageDTO;
import com.tanle.e_commerce.entities.ChatMessage;
import com.tanle.e_commerce.entities.ChatRoom;

import java.util.List;

public interface FirebaseService {
    String findChatRoomIdByRecipientAndSender(int senderId, int recipientId);

    ChatRoom getChatRoomBySenderAndRecipient(int senderId, int recipientId);

    ChatRoom createChatRoom(int senderId, int recipientId);
    List<ChatMessageDTO> getChatMessageByRoom(int senderId, int recipientId);
    ChatMessageDTO saveMessage(ChatMessage chatMessage, int senderId, int recipientId);
}
