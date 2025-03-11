package com.tanle.e_commerce.controller;

import com.tanle.e_commerce.dto.UserDTO;
import com.tanle.e_commerce.entities.MyUser;
import com.tanle.e_commerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:5173")
public class UserChatController {

    @MessageMapping("/user.connect")
    @SendTo("/user/public")
    public MyUser connect(
            @AuthenticationPrincipal MyUser myUser
    ) {
        return myUser;
    }

    @MessageMapping("/user.disconnect")
    @SendTo("/user/public")
    public MyUser disconnectUser(
            @AuthenticationPrincipal MyUser myUser
    ) {
        return myUser;
    }

}
