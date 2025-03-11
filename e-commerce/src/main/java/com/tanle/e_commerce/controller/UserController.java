package com.tanle.e_commerce.controller;

import com.tanle.e_commerce.dto.*;
import com.tanle.e_commerce.entities.Address;
import com.tanle.e_commerce.entities.MyUser;
import com.tanle.e_commerce.request.UpdateUserInforRequeset;
import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.service.CartService;
import com.tanle.e_commerce.service.OrderService;
import com.tanle.e_commerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "http://localhost:5173") // Allow frontend access
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

    @GetMapping("/chat")
    public ResponseEntity<List<UserDTO>> findUserChatById(@AuthenticationPrincipal MyUser myUser) {
        List<UserDTO> userDTO = userService.findUserChat(myUser.getId());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<UserDTO> findUserByUsername(@AuthenticationPrincipal MyUser user) {
        UserDTO userDTO = userService.findByUsername(user.getUsername());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    private ResponseEntity<MessageResponse> updateUserInfor(
            @AuthenticationPrincipal MyUser user,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "sex", required = false) Boolean sex,
            @RequestParam(value = "dob", required = false) String dobDay,
            @RequestParam(value = "avt", required = false) MultipartFile avtFile) {
        UpdateUserInforRequeset requeset = UpdateUserInforRequeset.builder()
                .dob(dobDay)
                .avt(avtFile)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .sex(sex)
                .build();
        MessageResponse response = userService.update(user, requeset);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/password")
    public ResponseEntity<MessageResponse> changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MessageResponse response = userService.changePassword(authentication, passwordChangeDTO);
        tokenSerice.registerToken(authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registerUser(@RequestBody RegisterUserDTO registerUserDTO) {
        UserDTO userDTO = userService.registerUser(registerUserDTO);
        MessageResponse tokenMessage = tokenSerice.registerToken(userDTO.getUsername());
        cartService.createCart(userDTO.getUserId());
        return new ResponseEntity<>(tokenMessage, HttpStatus.OK);
    }

    @PostMapping("/follow")
    public ResponseEntity<MessageResponse> followUser(@RequestBody Map<String, Integer> request) {
        MessageResponse userDTO = userService.followUser(request.get("userIdRequest"), request.get("followerId"));
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @PostMapping("/unfollow")
    public ResponseEntity<MessageResponse> unfollowUser(@RequestBody Map<String, Integer> request) {
        MessageResponse userDTO = userService.unfollowUser(request.get("userIdRequest"), request.get("followingId"));
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @PostMapping("/address")
    public ResponseEntity<AddressDTO> addAddress(@AuthenticationPrincipal MyUser user,
                                                 @RequestBody AddressDTO addressDTO) {
        AddressDTO address = userService.addAddress(user.getUsername(), addressDTO);
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @PutMapping("/address")
    public ResponseEntity<MessageResponse> updateAddress(@RequestBody AddressDTO addressDTO) {
        MessageResponse messageResponse = userService.updateAddress(addressDTO);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @DeleteMapping("/address")
    public ResponseEntity<MessageResponse> deleteAddress(@AuthenticationPrincipal MyUser user,
                                                         @RequestParam("addressId") String addressId) {
        MessageResponse messageResponse = userService.deleteAddress(
                user.getUsername()
                , Integer.parseInt(addressId));
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @GetMapping("/address")
    public ResponseEntity<List<AddressDTO>> getAddress(@AuthenticationPrincipal MyUser user) {
        List<AddressDTO> addressDTOS = userService.findAddressByUser(user.getUsername());
        return ResponseEntity.ok(addressDTOS);
    }


}
