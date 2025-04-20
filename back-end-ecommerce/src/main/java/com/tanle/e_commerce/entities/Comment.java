package com.tanle.e_commerce.entities;


import com.tanle.e_commerce.dto.CommentDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Comment {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "content")
    private String content;
    @Column(name = "create_at")
    private LocalDateTime createdAt;
    @Column(name = "rating")
    private int rating;
    @JoinColumn(name = "user_id")
    @ManyToOne
    private MyUser user;
    @ManyToOne
    @JoinColumn(name = "sku_id")
    private SKU sku;

}
