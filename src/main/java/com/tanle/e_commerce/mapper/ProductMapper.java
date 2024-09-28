package com.tanle.e_commerce.mapper;

import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.dto.SKUDTO;
import com.tanle.e_commerce.entities.Option;
import com.tanle.e_commerce.entities.OptionValue;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.entities.SKU;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {SKUMapper.class,CategoryMapper.class})
public interface ProductMapper {
    @Mapping(target = "price", expression = "java(mapPrice(product))")
    @Mapping(target = "stock",expression = "java(mapStock(product))")
    @Mapping(target = "options", expression = "java(mapOptions(product))")
    ProductDTO asInput(Product product);

    @Mapping(target = "options", expression = "java(mapOptionsBack(productDTO))")
    Product toEntity(ProductDTO productDTO);

    @Mapping(target = "options", expression = "java(mapOptionsBack(dto))")
    void update(@MappingTarget Product product, ProductDTO dto);

    default double mapPrice (Product product) {
        return product.getSkus().stream()
                .mapToDouble(SKU::getPrice)
                .min()
                .orElseGet(() ->0);
    }
    default int mapStock (Product product) {
        return product.getSkus().stream()
                .mapToInt(SKU::getStock)
                .sum();
    }
    default Map<String, Option> mapOptions(Product product) {
        if (product.getOptions() == null) return null;
        Map<String, Option> mpOption = new HashMap<>();
        product.getOptions().forEach(o -> mpOption.put(o.getName(),o));

        return mpOption;
    }

    default List<Option> mapOptionsBack(ProductDTO dto) {
        if(dto.getOptions() == null) return null;
        List<Option> options = new ArrayList<>();
        for (var item : dto.getOptions().entrySet()) {
            options.add(item.getValue());
        }
        return options;
    }
}

