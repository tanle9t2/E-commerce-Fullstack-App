package com.tanle.e_commerce.service;

import com.tanle.e_commerce.dto.UserDTO;
import com.tanle.e_commerce.entities.Address;
import com.tanle.e_commerce.entities.User;
import com.tanle.e_commerce.payload.MessageResponse;

import java.util.List;

public interface UserService {
    List<UserDTO> findAllUser();
    UserDTO findById(Integer id);
    void delete(Integer id);
    UserDTO update(User user);

    UserDTO followUser(Integer userId, Integer followingId);
    UserDTO unfollowUser(Integer userId, Integer followingId);
    UserDTO addAddress(Integer userId, Address address);

    MessageResponse updateAddress(Address address);
    MessageResponse deleteAddress(Integer userId, Integer addressId);
}
