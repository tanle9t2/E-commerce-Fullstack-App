package com.tanle.e_commerce.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatNotification {
    private int id;
    private int senderId;
    private int recipientId;
    private LocalDateTime createdAt;
    private String content;
}
