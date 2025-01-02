package com.tanle.e_commerce.mapper.decoratormapper;

import com.tanle.e_commerce.dto.SKUDTO;
import com.tanle.e_commerce.entities.Option;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.entities.SKU;
import com.tanle.e_commerce.mapper.SKUMapper;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Mapper
@NoArgsConstructor
public abstract class SKUMapperDecorator implements SKUMapper {
    @Qualifier("delegate")
    private SKUMapper delegate;

    @Override
    public SKU update(Product product, SKU sku) {
        List<Option> options = product.getOptions();
        for (var item : sku.getOptionValues()) {

        }
        return null;
    }
}
