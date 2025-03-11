package com.tanle.e_commerce.service.serviceimpl;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tanle.e_commerce.dto.ChatMessageDTO;
import com.tanle.e_commerce.entities.ChatMessage;
import com.tanle.e_commerce.entities.ChatRoom;
import com.tanle.e_commerce.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FirebaseServiceImpl implements FirebaseService {
    private final DatabaseReference messagesRef;
    private final DatabaseReference roomsRef;

    @Autowired
    public FirebaseServiceImpl(FirebaseDatabase firebaseDatabase) {
        this.messagesRef = firebaseDatabase.getReference("chat_messages");
        this.roomsRef = firebaseDatabase.getReference("chat_rooms");
    }

    @Override
    public String findChatRoomIdByRecipientAndSender(int senderId, int recipientId) {
        return null;
    }

    @Override
    public ChatRoom getChatRoomBySenderAndRecipient(int senderId, int recipientId) {
        return null;
    }

    @Override
    public ChatRoom createChatRoom(int senderId, int recipientId) {
        return null;
    }

    @Override
    public List<ChatMessageDTO> getChatMessageByRoom(int senderId, int recipientId) {
        return null;
    }

    @Override
    public ChatMessageDTO saveMessage(ChatMessage chatMessage, int senderId, int recipientId) {
        return null;
    }
}
