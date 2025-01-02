package com.tanle.e_commerce.Repository.Jpa;

import com.tanle.e_commerce.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer>,CustomCommentRepository {
    @Query("from Comment where product.id = ?1 ")
    List<Comment> getCommentByProduct(Integer productId);
    @Query(value = "select c1.* from comments c1 join comments c2 " +
            "ON c2.comment_id = ?1 " +
            "where c1.lft between c2.lft + 1 and c2.rgt;",nativeQuery = true)
    List<Comment> getSubComment(Integer commentId);

}
