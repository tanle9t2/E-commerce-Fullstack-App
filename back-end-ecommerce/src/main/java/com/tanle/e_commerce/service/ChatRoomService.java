package com.tanle.e_commerce.service;

import com.tanle.e_commerce.entities.ChatRoom;

public interface ChatRoomService {
    String findChatRoomIdByRecipientAndSender(int senderId, int recipientId);

    ChatRoom getChatRoomBySenderAndRecipient(int senderId, int recipientId);

    ChatRoom createChatRoom(int senderId, int recipientId);
}
