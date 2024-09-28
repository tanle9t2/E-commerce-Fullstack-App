package com.tanle.e_commerce.Repository.Jpa;

import com.tanle.e_commerce.entities.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

@Repository
public class CustomCategoryRepositoryImpl implements CustomCategoryRepository{
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public Category createCategory(Category parent, Category category) {
        int pos = parent.getRight();
        int left = pos;
        int right = pos+1;
        Query updateRight = entityManager.createNativeQuery(
                "Update category SET rgt = rgt +2 " +
                        "where rgt >= :pos ").setParameter("pos", pos);
        Query updateLeft = entityManager.createNativeQuery("Update category SET lft = lft +2 " +
                "where lft >= :pos").setParameter("pos" ,pos);
        updateLeft.executeUpdate();
        updateRight.executeUpdate();
        category.setLeft(left);
        category.setRight(right);

        entityManager.persist(category);
        return category;
    }
    @Override
    public void updateIndexDeleteCategory(int left, int right) {
        int step = right - left +1;
        Query updateRight = entityManager.createNativeQuery(
                        "Update category SET rgt = rgt - :step " +
                                "where rgt >= :pos ")
                .setParameter("step", step)
                .setParameter("pos", right);
        Query updateLeft = entityManager.createNativeQuery("Update category SET lft = lft - :step " +
                        "where lft >= :pos")
                .setParameter("step", step)
                .setParameter("pos", right);
        updateLeft.executeUpdate();
        updateRight.executeUpdate();
    }

}
