package com.tanle.e_commerce.Repository.Jpa;

import com.tanle.e_commerce.entities.Comment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

@Repository
public class CustomCommentRepositoryImpl implements CustomCommentRepository{
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public Comment createComment(Integer parentId, Comment comment) {
        Comment parent = entityManager.find(Comment.class,parentId);
        Query updateLeft = entityManager.createNativeQuery(
                        "Update comments SET lft = lft +2 where lft >= :pos")
                .setParameter("pos",parent.getRight());
        Query updateRight = entityManager.createNativeQuery(
                        "Update comments SET rgt = rgt +2 where rgt >= :pos")
                .setParameter("pos",parent.getRight());
        updateLeft.executeUpdate();
        updateRight.executeUpdate();
        comment.setLeft(parent.getRight());
        comment.setRight(parent.getRight()+1);
        entityManager.persist(comment);
        return comment;
    }

    @Override
    public void updateIndexDelete(int left, int right) {
        int step = right - left +1;
        Query updateRight = entityManager.createNativeQuery(
                        "Update comments SET rgt = rgt - :step " +
                                "where rgt >= :pos ")
                .setParameter("step", step)
                .setParameter("pos", right);
        Query updateLeft = entityManager.createNativeQuery("Update comments SET lft = lft - :step " +
                        "where lft >= :pos")
                .setParameter("step", step)
                .setParameter("pos", right);
        updateLeft.executeUpdate();
        updateRight.executeUpdate();
    }

}
