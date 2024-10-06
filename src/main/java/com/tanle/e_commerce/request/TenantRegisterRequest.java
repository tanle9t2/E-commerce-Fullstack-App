package com.tanle.e_commerce.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TenantRegisterRequest {
    private int tenantId;
    private String storeName;
    private String description;
    private Integer pickupAddressId;
    private Integer returnAddressId;
    private String phoneNumber;
    private String email;
}
