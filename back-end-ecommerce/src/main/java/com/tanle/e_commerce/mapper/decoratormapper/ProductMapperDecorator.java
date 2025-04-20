package com.tanle.e_commerce.mapper.decoratormapper;

import com.tanle.e_commerce.Repository.Jpa.CommentRepository;
import com.tanle.e_commerce.Repository.Jpa.OrderDetailRepository;
import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.dto.ProductDocument;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.mapper.CommentMapper;
import com.tanle.e_commerce.mapper.ProductMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.stream.Collectors;

@NoArgsConstructor
public abstract class ProductMapperDecorator implements ProductMapper {
    @Autowired
    private ProductMapper delegate;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public ProductDTO asInput(Product product) {
        ProductDTO productDTO = delegate.asInput(product);
        int sell = orderDetailRepository.sumProductSell(product.getId())
                .orElse(0);
        productDTO.setTotalSell(sell);
        return productDTO;
    }

    @Override
    public ProductDocument toDocument(Product product) {
        ProductDocument productDocument = delegate.toDocument(product);
        productDocument.setImages(product.getImages()
                .stream()
                .map(i -> new ProductDocument.ImageDocument(i.getImageId(), i.getImageUrl()))
                .collect(Collectors.toList()));
        ProductDocument.TenantDocument tenantDocument = new ProductDocument.TenantDocument();
        tenantDocument.setId(product.getTenant().getId());
        tenantDocument.setName(product.getTenant().getName());
        tenantDocument.setLocation(product.getTenant().getPickupAddress().getCity());

        productDocument.setTenantDocument(tenantDocument);
        return productDocument;
    }
}
