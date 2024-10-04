package com.tanle.e_commerce.mapper.decoratormapper;

import com.tanle.e_commerce.Repository.Jpa.AddressRepository;
import com.tanle.e_commerce.dto.TenantDTO;
import com.tanle.e_commerce.entities.Address;
import com.tanle.e_commerce.entities.Tenant;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.mapper.TenantMapper;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Mapper
@NoArgsConstructor
public abstract class TenantMapperDecorator implements TenantMapper{
    @Qualifier("deligate")
    private TenantMapper deligate;
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Tenant updateExisting(Tenant tenant, TenantDTO tenantDTO) {
        Tenant result = deligate.updateExisting(tenant,tenantDTO);
        if(tenantDTO.getPickupAddressId() != null) {
            result.setPickupAddress(addressRepository.findById(tenantDTO.getPickupAddressId())
                    .orElseThrow(() -> new ResourceNotFoundExeption("Not found pickup address")));
        }
        if(tenantDTO.getReturnAddressId() != null) {
            result.setReturnAddress(addressRepository.findById(tenantDTO.getReturnAddressId())
                    .orElseThrow(() -> new ResourceNotFoundExeption("Not found return address")));
        }
        return result;
    }
}
