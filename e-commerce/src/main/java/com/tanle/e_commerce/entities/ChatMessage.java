package com.tanle.e_commerce.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class ChatMessage {
    private String id;
    private String chatId;
    private int senderId;
    private int recipientId;
    private String content;
    private Long timestamp;
}
