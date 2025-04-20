package com.tanle.e_commerce.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class RegisterUserDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private boolean sex;
    private String email;
    private String phoneNumber;
}
