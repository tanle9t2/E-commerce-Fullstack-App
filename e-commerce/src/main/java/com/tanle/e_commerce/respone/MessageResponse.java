package com.tanle.e_commerce.respone;

import lombok.*;
import org.springframework.http.HttpStatus;
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MessageResponse {
    private HttpStatus status;
    private String message;
    private Object data;
}
