package com.tanle.e_commerce.mapper.decoratormapper;


import com.tanle.e_commerce.dto.UserDTO;
import com.tanle.e_commerce.entities.User;
import com.tanle.e_commerce.mapper.UserMapper;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


@NoArgsConstructor
@Mapper
public abstract class UserMapperDecorator implements UserMapper {

    @Autowired
    private UserMapper delegate;

    @Override
    public User convertEntity(UserDTO userDTO) {
        User user = delegate.convertEntity(userDTO);
        user.setFollowing(null);
        user.setFollowers(null);
        return user;
    }
}
