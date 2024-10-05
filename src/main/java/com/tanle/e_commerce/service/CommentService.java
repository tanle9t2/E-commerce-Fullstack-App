package com.tanle.e_commerce.service;

import com.tanle.e_commerce.entities.Comment;
import com.tanle.e_commerce.respone.MessageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommentService {
    ResponseEntity<Comment> findById(Integer id);
    ResponseEntity<List<Comment>> findAll();
    ResponseEntity<List<Comment>> findByProduct(Integer productId);
    ResponseEntity<List<Comment>> findByParentComment(Integer commnentId);
    ResponseEntity<Comment> createComment(Integer parentId, Comment comment);
    MessageResponse deleteComment(Integer commentId);
}
