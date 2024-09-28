package com.tanle.e_commerce.Repository.Jpa;

import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.payload.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    @Query(value = "from Product p JOIN Category c ON p.category.id = c.id "
            + "where (p.name like %?1% or p.description like %?1% or c.name like %?1% or c.description like %?1%)" +
            "and (?2 is null or c.name = ?2)"
    )
    Page<Product> findByKeyword(String keyword, String type, Double minPrice, Double maxPrice, Pageable pageable);

    Page<Product> findAllByName(String name, Pageable pageable);

    @Query("from Product as p where p.category.id in (?1)")
    Page<Product> findProductByCategory(String categoriesId, Pageable pageable);

    @Query("""
    select SUM (s.stock) FROM Product as p JOIN SKU as s 
        ON p.id = s.product.id
        where p.id = ?1
        group by (s.product.id)
    """ )
    Integer getTotalStock(int productId);

    @Query("""
       select MIN(s.price) from Product as p JOIN SKU as s
       On p.id= s.product.id
       where p.id = ?1
       GROUP BY (s.product.id)
    """)
    Double getMinPrice(int productId);

    @Query("from Product as p where p.tenant.id = ?1")
    Page<Product> findProductByTenant(Integer tenantId, Pageable pageable);

    @Query("select count(p.id) from Product as p where p.tenant.id = ?1")
    Long sumProductByTenet(Integer tenantId);
}
