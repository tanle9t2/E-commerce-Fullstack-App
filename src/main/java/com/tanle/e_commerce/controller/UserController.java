package com.tanle.e_commerce.controller;

import com.tanle.e_commerce.dto.PasswordChangeDTO;
import com.tanle.e_commerce.dto.RegisterUserDTO;
import com.tanle.e_commerce.dto.UserDTO;
import com.tanle.e_commerce.entities.Address;
import com.tanle.e_commerce.entities.Token;
import com.tanle.e_commerce.entities.User;
import com.tanle.e_commerce.payload.MessageResponse;
import com.tanle.e_commerce.request.LoginRequest;
import com.tanle.e_commerce.service.TokenSerice;
import com.tanle.e_commerce.service.UserService;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/user/")
public class UserController extends BaseUserController{
    @Autowired
    private UserService userService;
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable int userId) {
        UserDTO userDTO = userService.findById(userId);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
    @GetMapping("/")
    public ResponseEntity<UserDTO> findUserByUsername(@RequestParam(value = "username") String username) {
        UserDTO userDTO = userService.findByUsername(username);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
    @PostMapping("/registerToken")
    public ResponseEntity<MessageResponse> registerToken(@RequestParam("username") String username) {
        MessageResponse response = tokenSerice.registerToken(username);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @PostMapping("/password")
    public ResponseEntity<MessageResponse> changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MessageResponse response = userService.changePassword(authentication,passwordChangeDTO);
        tokenSerice.registerToken(authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registUser(@RequestBody RegisterUserDTO registerUserDTO) {
        UserDTO userDTO = userService.registerUser(registerUserDTO);
        tokenSerice.registerToken(userDTO.getUsername());
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }
    @PostMapping("/follow")
    public ResponseEntity<UserDTO> followUser(@RequestParam(name = "userId") String userId
            ,@RequestParam(name = "followingId") String followingId) {
        UserDTO userDTO = userService.followUser(Integer.parseInt(userId),Integer.parseInt(followingId));
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }
    @PostMapping("/unfollow")
    public ResponseEntity<UserDTO> unfollowUser(@RequestParam(name = "userId") String userId
            ,@RequestParam(name = "followingId") String followingId) {
        UserDTO userDTO = userService.unfollowUser(Integer.parseInt(userId),Integer.parseInt(followingId));
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }
    @PostMapping("/address")
    public ResponseEntity<UserDTO> addAddress(@RequestParam(value = "userId") String userId,
                                              @RequestBody Map<String, Object> data) {
        Address address = buildAddress(data);

        UserDTO userDTO = userService.addAddress(Integer.parseInt(userId),address);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }
    @PutMapping("/address")
    public ResponseEntity<MessageResponse> updateAddress(@RequestBody Map<String, Object> data) {
        Address address = buildAddress(data);
        MessageResponse messageResponse = userService.updateAddress(address);
        return new ResponseEntity<>(messageResponse,HttpStatus.OK);
    }
    @DeleteMapping("/address")
    public ResponseEntity<MessageResponse> deleteAddress(@RequestParam(value = "userId") String userId,
                                                         @RequestParam(value = "addressId") String addressId) {
        MessageResponse messageResponse = userService.deleteAddress(Integer.parseInt(userId)
                ,Integer.parseInt(addressId));

        return new ResponseEntity<>(messageResponse,HttpStatus.OK);
    }
    private Address buildAddress(Map<String,Object> data) {
        return Address.builder()
                .id(Integer.parseInt(data.get("id") != null ?data.get("id").toString():"0"))
                .city(String.valueOf(data.get("city")))
                .district(String.valueOf(data.get("district")))
                .street(String.valueOf(data.get("street")))
                .country(String.valueOf(data.get("country")))
                .firstName(String.valueOf(data.get("firstName")))
                .lastName(String.valueOf(data.get("lastName")))
                .phoneNumber(String.valueOf(data.get("phoneNumber")))
                .streetNumber(String.valueOf(data.get("streetNumber")))
                .build();
    }
}
