package com.tanle.e_commerce.service;

import com.tanle.e_commerce.dto.AddressDTO;
import com.tanle.e_commerce.dto.PasswordChangeDTO;
import com.tanle.e_commerce.dto.RegisterUserDTO;
import com.tanle.e_commerce.dto.UserDTO;
import com.tanle.e_commerce.entities.Address;
import com.tanle.e_commerce.entities.MyUser;
import com.tanle.e_commerce.request.UpdateUserInforRequeset;
import com.tanle.e_commerce.respone.MessageResponse;

import com.tanle.e_commerce.service.authorization.OwnerService;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService extends OwnerService<MyUser,Integer> {
    List<UserDTO> findAllUser();
    MessageResponse grantRole(Integer userId,String nameRole);
    UserDTO findById(Integer id);
    void delete(Integer id);
    void update(MyUser user, UpdateUserInforRequeset requeset);
    MessageResponse updateLastAccess(String username);

    MessageResponse followUser(Integer userId, Integer followingId);
    MessageResponse unfollowUser(Integer userId, Integer followingId);
    List<AddressDTO> findAddressByUser(String username);
    UserDTO addAddress(Integer userId, Address address);
    MessageResponse updateAddress(Address address);
    MessageResponse deleteAddress(Integer userId, Integer addressId);
    UserDTO registerUser(RegisterUserDTO registerUserDTO);

    MessageResponse changePassword(Authentication authentication, PasswordChangeDTO passwordChangeDTO);

    UserDTO findByUsername(String username);

}
