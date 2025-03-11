package com.tanle.e_commerce.mapper;

import com.tanle.e_commerce.dto.ChatMessageDTO;
import com.tanle.e_commerce.entities.ChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {

    @Mapping(target = "senderId",source = "chatRoom.sender.id")
    @Mapping(target = "recipientId",source = "chatRoom.recipient.id")
    ChatMessageDTO convertDTO(ChatMessage chatMessage);
    ChatMessage convertEntity(ChatMessageDTO chatMessageDTO);
}
