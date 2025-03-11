package com.tanle.e_commerce.service.serviceimpl;

import com.tanle.e_commerce.Repository.Jpa.ChatMessageRepository;
import com.tanle.e_commerce.dto.ChatMessageDTO;
import com.tanle.e_commerce.entities.ChatMessage;
import com.tanle.e_commerce.entities.ChatRoom;
import com.tanle.e_commerce.mapper.ChatMessageMapper;
import com.tanle.e_commerce.service.ChatMessageService;
import com.tanle.e_commerce.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Override
    public List<ChatMessageDTO> getChatMessageByRoom(int senderId, int recipientId) {
        String roomName = chatRoomService.findChatRoomIdByRecipientAndSender(senderId, recipientId);
        List<ChatMessage> chatMessages = chatMessageRepository.findChatMessageByChatRoomName(roomName);

        return chatMessages.stream()
                .sorted(Comparator.comparing(ChatMessage::getCreatedAt))
                .map(c -> chatMessageMapper.convertDTO(c))
                .collect(Collectors.toList());
    }

    @Override
    public ChatMessageDTO saveMessage(ChatMessage chatMessage, int senderId, int recipientId) {
        ChatRoom chatRoom = chatRoomService.getChatRoomBySenderAndRecipient(senderId, recipientId);
        LocalDateTime localDateTime = LocalDateTime.now();
        chatMessage.setCreatedAt(localDateTime);
        chatMessage.setChatRoom(chatRoom);
        return chatMessageMapper.convertDTO(chatMessageRepository.save(chatMessage));
    }
}
