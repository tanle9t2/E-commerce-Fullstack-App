package com.tanle.e_commerce.dao;

import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.payload.PageResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProductDAO{
    PageResponse<Product> findAll(Integer offset, Integer limit);
    List<Product> findByName(String nameProduct,Integer offset, Integer limit);
    List<Product> findByPrice(Double minPrice,Double maxPrice ,Integer offset, Integer limit);

    Optional<Product> findById(Integer id);

    List<Product> order(String direction,Integer offset, Integer limit, String ...field);
    Product update(Product product);
    void delete(Integer id);

}
