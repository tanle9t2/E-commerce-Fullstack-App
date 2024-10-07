package com.tanle.e_commerce.mapper;

import com.tanle.e_commerce.dto.UserDTO;
import com.tanle.e_commerce.entities.Follower;
import com.tanle.e_commerce.entities.User;
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
    User convertEntity(UserDTO userDTO);

    @Mapping(target = "userId", source = "id")
    @Mapping(target = "following" ,expression = "java(mapFollowing(user))")
    @Mapping(target = "follower" ,expression = "java(mapFollower(user))")
    UserDTO convertDTO(User user);
    default List<Follower> mapBackFollower() {

        return null;
    }

    default List<Follower> mapBackFollowing() {
        return null;
    }

    default List<Integer> mapFollower(User user) {
        if (user != null && user.getFollowers() != null) {
            List<Integer> follwerId = new ArrayList<>();
            for (var follower : user.getFollowers()) {
                if (follower.getUnfollowDate() == null)
                    follwerId.add(follower.getFollower().getId());
            }
            return follwerId;
        }
        return null;
    }

    default List<Integer> mapFollowing(User user) {
        if (user != null && user.getFollowers() != null) {
            List<Integer> follwerId = new ArrayList<>();
            for (var follower : user.getFollowing()) {
                if (follower.getUnfollowDate() == null)
                    follwerId.add(follower.getFollowing().getId());
            }
            return follwerId;
        }
        return null;
    }
}
