package com.tanle.e_commerce.service.serviceimpl;


import com.tanle.e_commerce.Repository.Jpa.AddressRepository;
import com.tanle.e_commerce.Repository.Jpa.RoleRepository;
import com.tanle.e_commerce.Repository.Jpa.UserRepository;
import com.tanle.e_commerce.dto.AddressDTO;
import com.tanle.e_commerce.dto.PasswordChangeDTO;
import com.tanle.e_commerce.dto.RegisterUserDTO;
import com.tanle.e_commerce.dto.UserDTO;
import com.tanle.e_commerce.entities.*;
import com.tanle.e_commerce.exception.ResourceExistedException;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.mapper.AddressMapper;
import com.tanle.e_commerce.mapper.UserMapper;
import com.tanle.e_commerce.request.UpdateUserInforRequeset;
import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.service.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.Format;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Autowired
    private UserMapper mapper;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public List<UserDTO> findAllUser() {
        return null;
    }

    @Override
    @Transactional
    public MessageResponse grantRole(Integer userId, String nameRole) {
        Role role = roleRepository.findRoleByRoleName(nameRole.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found role"));
        MyUser myUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found user"));
        myUser.addUserRole(role);

        return MessageResponse.builder()
                .status(HttpStatus.OK)
                .message("Successfully grant role " + role.getRoleName() + " for " + myUser.getUsername())
                .build();
    }

    @Override
    public UserDTO findById(Integer id) {
        MyUser myUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found user"));
        return mapper.convertDTO(myUser);
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    @Transactional
    public MessageResponse updateLastAccess(String username) {
        MyUser myUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found user"));
        myUser.updateLastAcess();
        return MessageResponse.builder()
                .message("Update last access successfully")
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public MessageResponse update(MyUser user, UpdateUserInforRequeset request) {
        Map<String, Object> updatedField = new HashMap<>();
        Optional.ofNullable(request.getFirstName()).ifPresent(firstName -> {
            user.setFirstName(firstName);
            user.setLastName(request.getLastName());
            updatedField.put("firstName", firstName);
            updatedField.put("lastName", request.getLastName());
        });

        Optional.ofNullable(request.getDob()).ifPresent(dob -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            updatedField.put("dob", dob);
            user.setDateOfBirth(LocalDate.parse(dob, formatter));
        });
        Optional.ofNullable(request.getPhoneNumber()).ifPresent(phone -> {
            updatedField.put("phone", phone);
            user.setPhoneNumber(phone);
        });
        Optional.ofNullable(request.getEmail()).ifPresent(email -> {
            updatedField.put("email", email);
            user.setEmail(email);
        });
        Optional.ofNullable(request.getSex()).ifPresent(sex -> {
            updatedField.put("sex", sex);
            user.setSex(sex);
        });
        Optional.ofNullable(request.getAvt()).ifPresent(avt -> {
            try {
                String url = cloudinaryService.uploadFile(avt);
                updatedField.put("avatar", url);
                user.setAvtUrl(url);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
        userRepository.save(user);
        return MessageResponse.builder()
                .data(updatedField)
                .message("Update successfully user")
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @Transactional
    public MessageResponse followUser(Integer userId, Integer followingId) {
        MyUser myUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found user"));
        MyUser following = userRepository.findById(followingId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found user"));
        myUser.followUser(following);
        userRepository.save(myUser);
        Map<String, Integer> date = Map.of(
                "Total following", myUser.countFollowing()
                , "Total follower", myUser.countFollower());
        return MessageResponse.builder()
                .data(date)
                .message("Follower user succefully")
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @Transactional
    public MessageResponse unfollowUser(Integer userId, Integer followingId) {
        MyUser myUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found user"));
        Follower unfollower = myUser.getFollowing().stream()
                .filter(f -> f.getFollowing().getId() == followingId && f.getUnfollowDate() == null)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found user"));
        myUser.unfollowUser(unfollower);
        Map<String, Integer> date = Map.of(
                "Total following", myUser.countFollowing()
                , "Total follower", myUser.countFollower());
        return MessageResponse.builder()
                .data(date)
                .message("Unfollower user succefully")
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public List<AddressDTO> findAddressByUser(String username) {
        MyUser myUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found username"));

        return myUser.getAddresses().stream()
                .filter(Address::isActive)
                .map(address -> addressMapper.convertDTO(address))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AddressDTO addAddress(String username, AddressDTO addressDTO) {
        MyUser myUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found user"));
        Address address = addressMapper.convertEntity(addressDTO);
        address.setActive(true);
        myUser.addAddress(address);
        userRepository.save(myUser);
        return addressMapper.convertDTO(address);
    }

    @Override
    @Transactional
    public MessageResponse updateAddress(AddressDTO addressDTO) {
        Address address = addressMapper.convertEntity(addressDTO);
        address.setActive(true);
        addressRepository.save(address);
        return MessageResponse.builder()
                .status(HttpStatus.OK)
                .message("Successfully update")
                .data(address)
                .build();
    }

    @Override
    @Transactional
    public MessageResponse deleteAddress(String username, Integer addressId) {
        MyUser myUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found user"));
        Optional<Address> isSuccess = myUser.getAddresses().stream()
                .filter(address -> address.getId() == addressId)
                .findFirst();
        if (!isSuccess.isPresent())
            throw new BadCredentialsException("Don't have permission");

        Address addressDB = isSuccess.get();
        addressDB.setActive(false);
        addressRepository.save(addressDB);
        return MessageResponse.builder()
                .message("Successfully delete Address")
                .status(HttpStatus.OK)
                .data(addressDB)
                .build();
    }

    @Override
    @Transactional
    public UserDTO registerUser(RegisterUserDTO registerUserDTO) {
        try {
            MyUser myUser = MyUser.builder()
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
            myUser.addUserRole(role);
            userRepository.save(myUser);
            return mapper.convertDTO(myUser);
        } catch (DataIntegrityViolationException exception) {
            throw new ResourceExistedException("username existed");
        }

    }

    @Override
    @Transactional
    public MessageResponse changePassword(Authentication authentication, PasswordChangeDTO passwordChangeDTO) {

        MyUser myUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Not found user"));
        if (!passwordEncoder.matches(passwordChangeDTO.getOldPassword(), myUser.getPassword()))
            throw new BadCredentialsException("Password invalid");
        if (!passwordChangeDTO.getNewPassword().equals(passwordChangeDTO.getConfirmPassword()))
            throw new BadCredentialsException("Password doesn't match with confirm password");

        myUser.setPassword(passwordEncoder.encode(passwordChangeDTO.getNewPassword()));
        userRepository.save(myUser);
        return MessageResponse.builder()
                .message("Change password successfully")
                .status(HttpStatus.OK)
                .build();


    }

    @Override
    public UserDTO findByUsername(String username) {
        MyUser myUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found user"));
        return mapper.convertDTO(myUser);
    }

    @Override
    public boolean userOwnEntity(Integer id, String username) {
        MyUser myUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found user"));
        return myUser.getUsername().equals(username);
    }
}
