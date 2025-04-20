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
import java.util.Map;
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
            SELECT node.id,node.name,node.left,node.right
            FROM Category as node, Category as parent
            WHERE node.left between parent.left and parent.right
            AND node.tenant.id=:tenantId
            GROUP BY node.id,node.name
            HAVING (count (parent.id)-1) =:level
            ORDER BY node.left
            """)
    List<Object[]> getCategoriesFollowLevel(@Param("tenantId") int tenantId, @Param("level") int level);

    @Query(value = """
            select c from Category as c 
            JOIN Category  as p on p.name =?1 
            where c.left between p.left+1 and p.right
            """)
    List<Category> getSubCategory(String name);

    @Query(value = """   
            SELECT node.product_category_id,node.name,node.lft,node.rgt
            FROM category AS node
            JOIN category AS parent ON node.lft BETWEEN parent.lft AND parent.rgt
            JOIN category AS sub_parent ON node.lft BETWEEN sub_parent.lft AND sub_parent.rgt
            JOIN (
                SELECT node.name, (COUNT(parent.name) - 1) AS depth
                FROM category AS node
                JOIN category AS parent ON node.lft BETWEEN parent.lft AND parent.rgt
                WHERE node.product_category_id =:parentId
                GROUP BY node.name, node.lft
            ) AS sub_tree
            WHERE node.lft BETWEEN parent.lft AND parent.rgt
                    AND node.lft BETWEEN sub_parent.lft AND sub_parent.rgt
                    AND sub_parent.name = sub_tree.name
                    and node.tenant_id =:tenantId
            GROUP BY node.name, node.lft, sub_tree.depth,node.rgt,node.product_category_id
            HAVING (COUNT(parent.name) - (sub_tree.depth + 1)) =:level
            ORDER BY node.lft;
            
            """, nativeQuery = true)
    List<Object[]> getSubcategoryFollowLevel(@Param("tenantId") Integer tenantId, @Param("level") Integer level, @Param("parentId") Integer parentId);

//    @Query(value = "")
//    CategoryDTO findByTenant(Integer tenantId, Integer level);
}
