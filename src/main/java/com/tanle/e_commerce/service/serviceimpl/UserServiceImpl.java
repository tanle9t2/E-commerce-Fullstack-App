package com.tanle.e_commerce.service.serviceimpl;


import com.tanle.e_commerce.Repository.Jpa.AddressRepository;
import com.tanle.e_commerce.Repository.Jpa.UserRepository;
import com.tanle.e_commerce.dto.UserDTO;
import com.tanle.e_commerce.entities.Address;
import com.tanle.e_commerce.entities.CompositeKey.FollowerKey;
import com.tanle.e_commerce.entities.Follower;
import com.tanle.e_commerce.entities.User;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.payload.MessageResponse;
import com.tanle.e_commerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<UserDTO> findAllUser() {
        return null;
    }

    @Override
    public UserDTO findById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found user"));
        return user.convertDTO();
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public UserDTO update(User user) {
        return null;
    }

    @Override
    @Transactional
    public UserDTO followUser(Integer userId, Integer followingId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found user"));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found user"));
        LocalDateTime followDate = LocalDateTime.now();

        FollowerKey followerKey = new FollowerKey(userId, followingId, followDate);
        Follower follower = Follower.builder()
                .followerKey(followerKey)
                .following(following)
                .follower(user)
                .build();

        user.getFollowing().add(follower);
        userRepository.save(user);
        return user.convertDTO();
    }

    @Override
    @Transactional
    public UserDTO unfollowUser(Integer userId, Integer followingId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found user"));
        Follower unfollower = user.getFollowing().stream()
                .filter(f -> f.getFollowing().getId() == followingId && f.getUnfollowDate() == null)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found"));
        LocalDateTime unfollowDate = LocalDateTime.now();
        unfollower.setUnfollowDate(unfollowDate);
        userRepository.save(user);
        return user.convertDTO();
    }

    @Override
    @Transactional
    public UserDTO addAddress(Integer userId, Address address) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found user"));
        user.getAddresses().add(address);
        userRepository.save(user);
        return user.convertDTO();
    }

    @Override
    @Transactional
    public MessageResponse updateAddress(Address address) {
        Address addressDB = addressRepository.save(address);
        return MessageResponse.builder()
                .status(HttpStatus.OK)
                .message("Successfully update")
                .data(addressDB)
                .build();
    }

    @Override
    @Transactional
    public MessageResponse deleteAddress(Integer userId, Integer addressId) {
        Address  addressDB = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found address"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found user"));
        user.getAddresses().remove(addressDB);
        addressRepository.delete(addressDB);

        return MessageResponse.builder()
                .message("Successfully delete Address")
                .status(HttpStatus.OK)
                .data(addressDB)
                .build();
    }

}
