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
}
