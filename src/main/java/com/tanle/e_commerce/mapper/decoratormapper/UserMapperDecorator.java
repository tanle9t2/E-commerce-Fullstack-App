package com.tanle.e_commerce.mapper.decoratormapper;


import com.tanle.e_commerce.dto.UserDTO;
import com.tanle.e_commerce.entities.MyUser;
import com.tanle.e_commerce.mapper.UserMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;


@NoArgsConstructor
public abstract class UserMapperDecorator implements UserMapper {
    @Qualifier("delegate")
    private UserMapper delegate;

    @Override
    public MyUser convertEntity(UserDTO userDTO) {
        MyUser myUser = delegate.convertEntity(userDTO);
        myUser.setFollowing(null);
        myUser.setFollowers(null);
        return myUser;
    }
}
