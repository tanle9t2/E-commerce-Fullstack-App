package com.tanle.e_commerce.respone;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AuthenticationRespone {
    private String accessToken;
    private String refreshToken;
    private UserInfor userInfor;

    @Builder
    @Data
    public static class UserInfor{
        private int id;
        private String username;
        private String email;
        private String avatar;
        private String fullName;
    }
}
