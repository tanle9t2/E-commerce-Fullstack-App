package com.tanle.e_commerce.mapper;

import com.tanle.e_commerce.dto.UserDTO;
import com.tanle.e_commerce.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface UserMapper {

    User convertEntity(UserDTO userDTO);
    UserDTO convertDTO(User user);
}
