package com.tanle.e_commerce.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CommentDTO {
    private int id;
    private String content;
    private int rating;
    private int productId;
    private LocalDateTime createdAt;
    private UserInfor userInfor;
    private String skuName;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class UserInfor {
        private String fullName;
        private String avtUrl;
    }

    public UserInfor newUserInfor(String fullName, String avt) {
        return new UserInfor(fullName, avt);
    }
}
