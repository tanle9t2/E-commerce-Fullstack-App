package com.tanle.e_commerce.mapper.decoratormapper;

import com.tanle.e_commerce.Repository.Jpa.SKURepository;
import com.tanle.e_commerce.dto.OrderDetailDTO;
import com.tanle.e_commerce.entities.OrderDetail;
import com.tanle.e_commerce.entities.SKU;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.mapper.OrderDetailMapper;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public abstract class OrderDetailMapperDecorator implements OrderDetailMapper {
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private SKURepository skuRepository;


    @Override
    public OrderDetail convertEntity(OrderDetailDTO orderDetailDTO) {
        SKU sku = skuRepository.findById(orderDetailDTO.getSkuId())
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found sku"));
        OrderDetail orderDetail = orderDetailMapper.convertEntity(orderDetailDTO);
        orderDetail.setSku(sku);
        return orderDetail;
    }
}
