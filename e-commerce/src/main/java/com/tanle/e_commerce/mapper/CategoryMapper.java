package com.tanle.e_commerce.mapper;

import com.tanle.e_commerce.dto.CategoryDTO;
import com.tanle.e_commerce.entities.Category;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.entities.Tenant;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.mapper.decoratormapper.CategoryMapperDecorator;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {TenantMapper.class, ProductMapper.class})
@DecoratedWith(CategoryMapperDecorator.class)
public interface CategoryMapper {

    @Mapping(target = "tenantId",source = "tenant.id")
    @Mapping(target = "productsId", expression = "java(mapProduct(category))")
    CategoryDTO convertDTO(Category category);

    Category convertEntity(CategoryDTO categoryDTO);

    Category update(String name,String description ,@MappingTarget Category category);

    default List<Integer> mapProduct(Category category) {
        if(category.getProducts() == null) return null;
        return category.getProducts().stream()
                .map(Product::getId)
                .collect(Collectors.toList());
    }
}
