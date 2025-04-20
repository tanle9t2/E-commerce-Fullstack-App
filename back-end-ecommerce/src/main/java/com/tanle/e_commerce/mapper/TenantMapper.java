package com.tanle.e_commerce.mapper;

import com.tanle.e_commerce.dto.TenantDTO;
import com.tanle.e_commerce.entities.Tenant;
import com.tanle.e_commerce.mapper.decoratormapper.TenantMapperDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
@DecoratedWith(TenantMapperDecorator.class)
public interface TenantMapper {
    @Mapping(target = "follower", expression = "java(mapFollower(tenant))")
    @Mapping(target = "following", expression = "java(mapFollowing(tenant))")
    @Mapping(target = "totalProduct", expression = "java(mapTotalProduct(tenant))")
    @Mapping(target = "userId",source = "tenant.myUser.id")
    @Mapping(target = "pickupAddressId",source = "tenant.pickupAddress.id")
    @Mapping(target = "returnAddressId",source = "tenant.returnAddress.id")
    TenantDTO convertDTO(Tenant tenant);

    Tenant converEntity(TenantDTO tenantDTO);

    Tenant updateExisting(@MappingTarget Tenant tenant, TenantDTO tenantDTO);

    default long mapTotalProduct(Tenant tenant) {
        return tenant.getProducts().size();
    }
    default long mapFollower(Tenant tenant) {
        return tenant.getMyUser()
                .getFollowers().stream()
                .filter(f -> f.getUnfollowDate() == null)
                .count();
    }
    default long mapFollowing(Tenant tenant) {
        return tenant.getMyUser()
                .getFollowing().stream()
                .filter(f -> f.getUnfollowDate() == null)
                .count();
    }
}
