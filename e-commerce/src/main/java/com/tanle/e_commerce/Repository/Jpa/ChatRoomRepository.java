package com.tanle.e_commerce.Repository.Jpa;

import com.tanle.e_commerce.entities.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Integer> {

    @Query("""
                from ChatRoom 
                where sender.id =:senderId and  recipient.id =:recipientId
            """)
    Optional<ChatRoom> findByRecipientAndSender(@Param("senderId") int senderId,
                                                @Param("recipientId")  int recipientId);
}
