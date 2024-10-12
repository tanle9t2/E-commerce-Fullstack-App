package com.tanle.e_commerce.service.serviceimpl;

import com.tanle.e_commerce.Repository.Jpa.UserRepository;
import com.tanle.e_commerce.entities.User;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import org.hibernate.annotations.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User myUser = userRepository.findByUsername(username).
                orElseThrow(() -> new ResourceNotFoundExeption("Not found user: " + username));
        return org.springframework.security.core.userdetails.User.withUsername(myUser.getUsername())
                .password(myUser.getPassword())
                .authorities(myUser.getAuthorities())
                .build();
    }
}
