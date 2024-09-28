package com.tanle.e_commerce.mapper;

import com.tanle.e_commerce.dto.TenantDTO;
import com.tanle.e_commerce.entities.Follower;
import com.tanle.e_commerce.entities.Tenant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface TenantMapper {
    @Mapping(target = "follower", expression = "java(mapFollower(tenant))")
    @Mapping(target = "following", expression = "java(mapFollowing(tenant))")
    @Mapping(target = "totalProduct", expression = "java(mapTotalProduct(tenant))")
    TenantDTO convertDTO(Tenant tenant);

    Tenant converEntity(TenantDTO tenantDTO);

    default long mapTotalProduct(Tenant tenant) {
        return tenant.getProducts().size();
    }
    default long mapFollower(Tenant tenant) {
        return tenant.getUser()
                .getFollowers().stream()
                .filter(f -> f.getUnfollowDate() == null)
                .count();
    }
    default long mapFollowing(Tenant tenant) {
        return tenant.getUser()
                .getFollowing().stream()
                .filter(f -> f.getUnfollowDate() == null)
                .count();
    }
}
