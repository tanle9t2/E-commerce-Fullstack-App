package com.tanle.e_commerce.Repository.Jpa;

import com.tanle.e_commerce.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("from Comment where sku.product.id = :productId ")
    Page<Comment> getCommentByProduct(@Param("productId") Integer productId, Pageable pageable);

    @Query("""
                select count(c.id) from Tenant t, Product p, Comment c
                where t.id = p.tenant.id
                and t.id =:tenantId
                and p.id = c.sku.product.id
            """)
    Long sumCommentByTenant(@Param("tenantId") Integer tenantId);
}
