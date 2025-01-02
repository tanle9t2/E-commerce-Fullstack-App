package com.tanle.e_commerce.service.serviceimpl;

import com.tanle.e_commerce.Repository.Jpa.UserRepository;
import com.tanle.e_commerce.entities.MyUser;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser myUser = userRepository.findByUsername(username).
                orElseThrow(() -> new ResourceNotFoundExeption("Not found user: " + username));
        return org.springframework.security.core.userdetails.User.withUsername(myUser.getUsername())
                .password(myUser.getPassword())
                .authorities(myUser.getAuthorities())
                .build();
    }
}
