package com.tanle.e_commerce.mapper.decoratormapper;

import com.tanle.e_commerce.Repository.Jpa.CommentRepository;
import com.tanle.e_commerce.Repository.Jpa.OrderDetailRepository;
import com.tanle.e_commerce.dto.ProductDTO;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.mapper.CommentMapper;
import com.tanle.e_commerce.mapper.ProductMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@NoArgsConstructor
public abstract class ProductMapperDecorator implements ProductMapper {
    @Autowired
    private ProductMapper delegate;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentMapper commentMapper;


    @Override
    public ProductDTO asInput(Product product) {
        ProductDTO productDTO = delegate.asInput(product);
        int sell = orderDetailRepository.sumProductSell(product.getId())
                .orElse(0);
        productDTO.setTotalSell(sell);
        return productDTO;
    }
}
