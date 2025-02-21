package com.tanle.e_commerce.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddressDTO {
    private int id;
    private String city;

    private String district;


    private String streetNumber;

    private String firstName;

    private String lastName;

    private String phoneNumber;
    private String ward;
}
