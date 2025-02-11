package com.tanle.e_commerce.mapper;

import com.tanle.e_commerce.dto.UserDTO;
import com.tanle.e_commerce.entities.Follower;
import com.tanle.e_commerce.entities.MyUser;
import com.tanle.e_commerce.mapper.decoratormapper.UserMapperDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
@DecoratedWith(UserMapperDecorator.class)
public interface UserMapper {
    @Mapping(target = "following" ,expression = "java(mapBackFollowing())")
    @Mapping(target = "followers" ,expression = "java(mapBackFollower())")
    MyUser convertEntity(UserDTO userDTO);

    @Mapping(target = "userId", source = "id")
    @Mapping(target = "username",source = "username")
    @Mapping(target = "following" ,expression = "java(mapFollowing(myUser))")
    @Mapping(target = "follower" ,expression = "java(mapFollower(myUser))")
    UserDTO convertDTO(MyUser myUser);
    default List<Follower> mapBackFollower() {

        return null;
    }

    default List<Follower> mapBackFollowing() {
        return null;
    }

    default List<Integer> mapFollower(MyUser myUser) {
        if (myUser != null && myUser.getFollowers() != null) {
            List<Integer> follwerId = new ArrayList<>();
            for (var follower : myUser.getFollowers()) {
                if (follower.getUnfollowDate() == null)
                    follwerId.add(follower.getFollower().getId());
            }
            return follwerId;
        }
        return null;
    }

    default List<Integer> mapFollowing(MyUser myUser) {
        if (myUser != null && myUser.getFollowers() != null) {
            List<Integer> follwerId = new ArrayList<>();
            for (var follower : myUser.getFollowing()) {
                if (follower.getUnfollowDate() == null)
                    follwerId.add(follower.getFollowing().getId());
            }
            return follwerId;
        }
        return null;
    }
}
