package com.tanle.e_commerce.mapper;

import com.tanle.e_commerce.dto.SKUDTO;
import com.tanle.e_commerce.entities.Option;
import com.tanle.e_commerce.entities.OptionValue;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.entities.SKU;
import com.tanle.e_commerce.mapper.decoratormapper.SKUMapperDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
@DecoratedWith(SKUMapperDecorator.class)
public interface SKUMapper {
    @Mapping(target = "optionValueIndex", expression = "java(mapOptionValue(sku))")
    @Mapping(target = "itemName", source = "sku.product.name")
    SKUDTO convertDTO(SKU sku);

    @Mapping(target = "optionValues", expression = "java(mapOptionValuesBack(skudto,product))")
    @Mapping(target = "id", source = "skudto.id")
    @Mapping(target = "product", source = "product")
    SKU convertEntity(SKUDTO skudto, Product product);

    SKU update(Product product, @MappingTarget SKU sku);

    default List<OptionValue> mapOptionValuesBack(SKUDTO sku, Product product) {
        if (product == null || sku == null) return null;
        List<OptionValue> optionValues = new ArrayList<>();
        for (int i = 0; i < sku.getOptionValueIndex().size(); i++) {
            OptionValue optionValue = product.getOptions().get(i).getOptionValues()
                    .get(sku.getOptionValueIndex().get(i));
            optionValues.add(optionValue);
        }
        return optionValues;
    }

    default List<Integer> mapOptionValue(SKU sku) {
        if (sku == null) return null;
        List<Integer> optionValueIndex = new ArrayList<>();
        Product product = sku.getProduct();
        List<OptionValue> optionValues = sku.getOptionValues();
        if (optionValues.isEmpty()) return null;
        int i = 0;
        for (Option option : product.getOptions()) {
            optionValueIndex.add(option.parseIndexOptionValue(optionValues.get(i)));
            i++;
        }
        return optionValueIndex;
    }
}
