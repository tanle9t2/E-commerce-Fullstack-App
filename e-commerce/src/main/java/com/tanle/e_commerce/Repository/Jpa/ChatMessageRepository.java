package com.tanle.e_commerce.Repository.Jpa;

import com.tanle.e_commerce.entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
    @Query("""
        from ChatMessage cm, ChatRoom  cr
        where cm.chatRoom.id = cr.id
        AND cr.chatRoomName =:chatRoomName
    """)
    List<ChatMessage> findChatMessageByChatRoomName(@Param("chatRoomName") String chatRoomName);


}
