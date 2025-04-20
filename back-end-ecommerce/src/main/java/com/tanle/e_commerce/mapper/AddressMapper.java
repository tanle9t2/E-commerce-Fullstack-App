package com.tanle.e_commerce.mapper;

import com.tanle.e_commerce.dto.AddressDTO;
import com.tanle.e_commerce.entities.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDTO convertDTO(Address address);
    Address convertEntity(AddressDTO addressDTO);
}
