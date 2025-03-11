package com.tanle.e_commerce.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "chat_room")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private int id;
    @Column(name = "chat_room_name")
    private String chatRoomName;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private MyUser sender;

    @ManyToOne
    @JoinColumn(name = "recipientId")
    private MyUser recipient;
}
