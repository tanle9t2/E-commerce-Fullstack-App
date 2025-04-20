package com.tanle.e_commerce.Repository.Jpa;

import com.tanle.e_commerce.dto.SKUDTO;
import com.tanle.e_commerce.entities.Option;
import com.tanle.e_commerce.entities.Product;
import com.tanle.e_commerce.entities.SKU;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SKURepository extends JpaRepository<SKU,Integer> {
    @Query(
            value = """
            select s.* from sku s JOIN sku_attribute sa
            ON s.sku_id = sa.sku_id
            Group by s.sku_id
            having group_concat(sa.option_value_id) = ?1
            """,nativeQuery = true
    )
    SKU getSKUByValue(String optionValue);
    List<SKU> getSKUSByProduct(Product product);
    Optional<SKU> findBySkuNo(String SkuNo);
}

