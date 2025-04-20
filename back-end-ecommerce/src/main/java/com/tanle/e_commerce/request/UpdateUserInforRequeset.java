package com.tanle.e_commerce.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class UpdateUserInforRequeset {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String dob;
    private Boolean sex;
    private MultipartFile avt;
}
