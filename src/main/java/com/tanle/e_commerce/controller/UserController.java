package com.tanle.e_commerce.controller;

import com.tanle.e_commerce.dto.OrderDTO;
import com.tanle.e_commerce.dto.PasswordChangeDTO;
import com.tanle.e_commerce.dto.RegisterUserDTO;
import com.tanle.e_commerce.dto.UserDTO;
import com.tanle.e_commerce.entities.Address;
import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.service.CartService;
import com.tanle.e_commerce.service.OrderService;
import com.tanle.e_commerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user/")
public class UserController extends BaseUserController {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;

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

    @PostMapping("/password")
    public ResponseEntity<MessageResponse> changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MessageResponse response = userService.changePassword(authentication, passwordChangeDTO);
        tokenSerice.registerToken(authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registUser(@RequestBody RegisterUserDTO registerUserDTO) {
        UserDTO userDTO = userService.registerUser(registerUserDTO);
        tokenSerice.registerToken(userDTO.getUsername());
        cartService.createCart(userDTO.getUserId());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/purchase")
    public ResponseEntity<List<OrderDTO>> getOrderByUser(@RequestBody Map<String,Integer> request
            , @RequestParam(name = "type", required = false) String type) {
        List<OrderDTO> orderDTOS = orderService.getPurchaseUser(request, type);
        return ResponseEntity.status(HttpStatus.OK).body(orderDTOS);
    }
    @PostMapping("/follow")
    public ResponseEntity<MessageResponse> followUser(@RequestBody Map<String, Integer> request) {
        MessageResponse userDTO = userService.followUser(request.get("userIdRequest"),request.get("followerId"));
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }
    @PostMapping("/unfollow")
    public ResponseEntity<MessageResponse> unfollowUser(@RequestBody Map<String, Integer> request) {
        MessageResponse userDTO = userService.unfollowUser(request.get("userIdRequest"),request.get("followingId"));
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @PostMapping("/address")
    public ResponseEntity<UserDTO> addAddress(@RequestParam(value = "userId") String userId,
                                              @RequestBody Map<String, Object> data) {
        Address address = buildAddress(data);
        UserDTO userDTO = userService.addAddress(Integer.parseInt(userId), address);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping("/address")
    public ResponseEntity<MessageResponse> updateAddress(@RequestBody Map<String, Object> data) {
        Address address = buildAddress(data);
        MessageResponse messageResponse = userService.updateAddress(address);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @DeleteMapping("/address")
    public ResponseEntity<MessageResponse> deleteAddress(@RequestBody Map<String, Integer> request) {
        MessageResponse messageResponse = userService.deleteAddress(
                request.get("userIdRequest")
                , request.get("addressId"));
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    private Address buildAddress(Map<String, Object> data) {
        return Address.builder()
                .id(Integer.parseInt(data.get("id") != null ? data.get("id").toString() : "0"))
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
