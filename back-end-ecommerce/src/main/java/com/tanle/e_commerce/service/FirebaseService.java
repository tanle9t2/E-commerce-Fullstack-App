package com.tanle.e_commerce.service;

import com.tanle.e_commerce.dto.ChatMessageDTO;
import com.tanle.e_commerce.entities.ChatMessage;
import com.tanle.e_commerce.entities.ChatRoom;

import java.util.List;

public interface FirebaseService {
    String findChatRoomIdByRecipientAndSender(int senderId, int recipientId);

    String createChatRoom(int senderId, int recipientId);
    List<ChatMessage> getChatMessageByRoom(int senderId, int recipientId);
    ChatMessage saveMessage(ChatMessage chatMessage, int senderId, int recipientId);

    List<ChatRoom> getAllChatRoomSender(int senderId);
    void  updateChatRoomStatus(String roomName,int userId,boolean isRead);
}
