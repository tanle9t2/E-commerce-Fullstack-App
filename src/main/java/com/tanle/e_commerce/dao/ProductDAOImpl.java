package com.tanle.e_commerce.dao;

import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.payload.PageResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ProductDAOImpl implements ProductDAO{
    @Autowired
    private EntityManager entityManager;

    @Override
    public PageResponse<Product> findAll(Integer offset, Integer limit) {

        Long count = (Long) entityManager.createQuery("select count(*) from Product ")
                .getSingleResult();
        List<Product> products = entityManager.createQuery("from Product ", Product.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
//        if (products.isEmpty()) {
//            return new PageResponse<>(Collections.emptyList(), offset / limit + 1, products.size(), count);
//        }
//        return new PageResponse<>(products, offset / limit + 1, products.size(), count);
        return null;
    }
    @Override
    public List<Product> findByName(String nameProduct, Integer offset, Integer limit) {
        TypedQuery<Product> query = entityManager.createQuery("from Product where name like %:data%", Product.class);
        query.setParameter("data", nameProduct);
        return query.getResultList();
    }

    @Override
    public Optional<Product> findById(Integer id) {
        return Optional.ofNullable(entityManager.find(Product.class, id));
    }

    @Override
    public List<Product> findByPrice(Double minPrice, Double maxPrice, Integer offset, Integer limit) {
        Long count = (Long) entityManager.createQuery("select count(*) from Product" +
                        " where price BETWEEN :minPrice and :maxPrice ")
                .setParameter("minPrice",minPrice)
                .getSingleResult();
        TypedQuery<Product> query = entityManager.createQuery("from Product  " +
                        "where price BETWEEN :minPrice and :maxPrice", Product.class)
                .setFirstResult(offset)
                .setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public List<Product> order(String direction, Integer offset, Integer limit, String... field) {
        TypedQuery<Product> query;
        if (direction == "ASC") {
            query = entityManager.createQuery("from Product order by :field asc ", Product.class)
                    .setParameter("field", String.join(",", field))
                    .setFirstResult(offset)
                    .setMaxResults(limit);
            return query.getResultList();
        }
        return entityManager.createQuery("from Product order by :field desc ", Product.class)
                .setParameter("field", field)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public Product update(Product product) {
        Product result = entityManager.merge(product);
        return result;
    }

    @Override
    public void delete(Integer id) {
        Optional<Product> product = findById(id);
        if (product.isPresent())
            entityManager.remove(product);
    }


}
