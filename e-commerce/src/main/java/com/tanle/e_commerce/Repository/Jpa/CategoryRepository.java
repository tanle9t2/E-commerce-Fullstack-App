package com.tanle.e_commerce.Repository.Jpa;

import com.tanle.e_commerce.entities.Category;
import com.tanle.e_commerce.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>
        , JpaSpecificationExecutor<Category>,
        CustomCategoryRepository {

    @Query(value = "select c from Category as c where c.id =?1 and c.tenant.id =?2")
    Optional<Category> byIdAndTenant(Integer parentId, Integer tenantId);
    @Query(value = "select c from Category as c where c.tenant.id =?1")
    List<Category> findByTenantId(Integer tenantId);
    @Query(value = """
                SELECT parent.name
                FROM Category AS node,
                        Category AS parent
                WHERE node.left BETWEEN parent.left AND parent.right
                        AND node.id =:categoryId
                ORDER BY parent.left
            """)
    List<String> getPath(@Param("categoryId") int categoryId);

    @Query(value = """
            select c from Category as c 
            JOIN Category  as p on p.name =?1 
            where c.left-1 between p.left and p.right""")
    Page<Category> getSubCategory(String name, Pageable pageable);

//    @Query(value = "")
//    CategoryDTO findByTenant(Integer tenantId, Integer level);
}
