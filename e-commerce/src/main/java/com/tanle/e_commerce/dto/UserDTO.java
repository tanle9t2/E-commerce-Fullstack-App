package com.tanle.e_commerce.dto;

import com.tanle.e_commerce.entities.Address;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class UserDTO {
    private int userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String avtUrl;
    private boolean isActive;
    private LocalDateTime dateOfBirth;
    private LocalDateTime lastAccess;
    private LocalDateTime createdAt;
    private List<Address> addresses;
    private List<Integer> following;
    private List<Integer> follower;
}
