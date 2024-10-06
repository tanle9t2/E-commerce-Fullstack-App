package com.tanle.e_commerce.service.serviceimpl;


import com.tanle.e_commerce.Repository.Jpa.AddressRepository;
import com.tanle.e_commerce.Repository.Jpa.RoleRepository;
import com.tanle.e_commerce.Repository.Jpa.UserRepository;
import com.tanle.e_commerce.dto.PasswordChangeDTO;
import com.tanle.e_commerce.dto.RegisterUserDTO;
import com.tanle.e_commerce.dto.UserDTO;
import com.tanle.e_commerce.entities.*;
import com.tanle.e_commerce.entities.CompositeKey.FollowerKey;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<UserDTO> findAllUser() {
        return null;
    }

    @Override
    @Transactional
    public MessageResponse grantRole(Integer userId,String nameRole) {
        Role role  = roleRepository.findRoleByRoleName(nameRole.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found role"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found user"));
        user.addUserRole(role);

        return MessageResponse.builder()
                .status(HttpStatus.OK)
                .message("Successfully grant role " + role.getRoleName()+ " for " + user.getUsername())
                .build();
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
    @Transactional
    public MessageResponse updateLastAccess(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found user"));
        user.updateLastAcess();
        return MessageResponse.builder()
                .message("Update last access successfully")
                .status(HttpStatus.OK)
                .build();
    }
    @Override
    public UserDTO update(UserDTO userDTO) {
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

    @Override
    @Transactional
    public UserDTO registerUser(RegisterUserDTO registerUserDTO) {
        User user = User.builder()
                .username(registerUserDTO.getUsername())
                .password(passwordEncoder.encode(registerUserDTO.getPassword()))
                .firstName(registerUserDTO.getFirstName())
                .lastName(registerUserDTO.getLastName())
                .phoneNumber(registerUserDTO.getPhoneNumber())
                .sex(registerUserDTO.isSex())
                .email(registerUserDTO.getEmail())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();
        Role role = roleRepository.findRoleByRoleName("CUSTOMER")
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found role"));
        user.addUserRole(role);
        userRepository.save(user);

        return user.convertDTO();
    }

    @Override
    @Transactional
    public MessageResponse changePassword(Authentication authentication, PasswordChangeDTO passwordChangeDTO) {
      try {
          if(authentication.getName() != passwordChangeDTO.getUsername())
              throw new BadCredentialsException("Username/password invalid");
          User user = userRepository.findByUsername(passwordChangeDTO.getUsername()).get();

          if(!passwordEncoder.matches(passwordChangeDTO.getOldPassword(), user.getPassword()))
              throw new BadCredentialsException("Username/password invalid");

          user.setPassword(passwordEncoder.encode(passwordChangeDTO.getNewPassword()));
          userRepository.save(user);
          return MessageResponse.builder()
                  .message("Change password successfully")
                  .status(HttpStatus.OK)
                  .build();

      }catch (BadCredentialsException e) {
          throw new BadCredentialsException(e.getMessage());
      }
    }

    @Override
    public UserDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found user"));
        return user.convertDTO();
    }

    @Override
    public boolean userOwnEntity(Integer id, String username) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found user"));
        return user.getUsername().equals(username);
    }
}
