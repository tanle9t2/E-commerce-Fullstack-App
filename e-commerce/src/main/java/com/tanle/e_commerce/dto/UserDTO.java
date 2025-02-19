package com.tanle.e_commerce.dto;

import com.tanle.e_commerce.entities.Address;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
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
    private String phoneNumber;
    private boolean sex;
    private boolean isActive;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;
    private LocalDateTime lastAccess;
    private LocalDateTime createdAt;
    private List<Address> addresses;
    private List<Integer> following;
    private List<Integer> follower;
}
