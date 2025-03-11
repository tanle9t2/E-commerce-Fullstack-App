package com.tanle.e_commerce.service.serviceimpl;

import com.tanle.e_commerce.Repository.Jpa.ChatRoomRepository;
import com.tanle.e_commerce.Repository.Jpa.UserRepository;
import com.tanle.e_commerce.entities.ChatRoom;
import com.tanle.e_commerce.entities.MyUser;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public String findChatRoomIdByRecipientAndSender(int senderId, int recipientId) {
        String chatRoom = chatRoomRepository.findByRecipientAndSender(senderId, recipientId)
                .map(ChatRoom::getChatRoomName)
                .orElseGet(() -> createChatRoom(senderId, recipientId).getChatRoomName());
        return chatRoom;
    }

    @Override
    public ChatRoom getChatRoomBySenderAndRecipient(int senderId, int recipientId) {
        ChatRoom chatRoom = chatRoomRepository.findByRecipientAndSender(senderId, recipientId)
                .orElseGet(() -> createChatRoom(senderId, recipientId));
        return chatRoom;
    }

    @Transactional
    @Override
    public ChatRoom createChatRoom(int senderId, int recipientId) {
        MyUser sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found sender"));
        MyUser recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found sender"));
        String chatRoomName = String.format("%s_%s", sender.getUsername(), recipient.getUsername());
        ChatRoom senderChatRoom = ChatRoom.builder()
                .chatRoomName(chatRoomName)
                .sender(sender)
                .recipient(recipient)
                .build();

        ChatRoom recipientChatRoom = ChatRoom.builder()
                .chatRoomName(chatRoomName)
                .sender(recipient)
                .recipient(sender)
                .build();

        chatRoomRepository.saveAll(List.of(senderChatRoom, recipientChatRoom));
        return senderChatRoom;
    }
}
