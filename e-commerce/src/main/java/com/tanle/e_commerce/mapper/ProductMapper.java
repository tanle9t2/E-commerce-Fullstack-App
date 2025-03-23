package com.tanle.e_commerce.mapper;

import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.dto.ProductDocument;
import com.tanle.e_commerce.dto.SKUDTO;
import com.tanle.e_commerce.entities.Option;
import com.tanle.e_commerce.entities.OptionValue;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.entities.SKU;
import com.tanle.e_commerce.mapper.decoratormapper.ProductMapperDecorator;
import com.tanle.e_commerce.mapper.decoratormapper.SKUMapperDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {SKUMapper.class, CategoryMapper.class})
@DecoratedWith(ProductMapperDecorator.class)
public interface ProductMapper {
    @Mapping(target = "minPrice", expression = "java(mapMinPrice(product))")
    @Mapping(target = "maxPrice", expression = "java(mapMaxPrice(product))")
    @Mapping(target = "stock", expression = "java(mapStock(product))")
    @Mapping(target = "options", expression = "java(mapOptions(product))")
    @Mapping(target = "tenantId",source = "product.tenant.id")
    ProductDTO asInput(Product product);

    @Mapping(target = "options", expression = "java(mapOptionsBack(productDTO))")
    Product toEntity(ProductDTO productDTO);
    @Mapping(target = "options", expression = "java(mapOptions(product))")
    @Mapping(target = "minPrice", expression = "java(mapMinPrice(product))")
    @Mapping(target = "maxPrice", expression = "java(mapMaxPrice(product))")
    @Mapping(target = "stock", expression = "java(mapStock(product))")
    ProductDocument toDocument(Product product);


    @Mapping(target = "options", expression = "java(mapOptionsBack(dto))")
    void update(@MappingTarget Product product, ProductDTO dto);

    default double mapMinPrice(Product product) {
        return product.getMinPrice();
    }
    default double mapMaxPrice(Product product) {
        return product.getMaxPrice();
    }


    default int mapStock(Product product) {
        return product.getSkus().stream()
                .mapToInt(SKU::getStock)
                .sum();
    }

    default Map<String, Option> mapOptions(Product product) {
        if (product.getOptions() == null) return null;
        Map<String, Option> mpOption = new HashMap<>();
        product.getOptions().forEach(o -> mpOption.put(o.getName(), o));

        return mpOption;
    }

    default List<Option> mapOptionsBack(ProductDTO dto) {
        if (dto.getOptions() == null) return null;
        List<Option> options = new ArrayList<>();
        for (var item : dto.getOptions().entrySet()) {
            options.add(item.getValue());
        }
        return options;
    }
}

