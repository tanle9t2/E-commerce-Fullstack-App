package com.tanle.e_commerce.service;

import com.tanle.e_commerce.dto.CommentDTO;
import com.tanle.e_commerce.entities.Comment;
import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.respone.PageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommentService {
    CommentDTO findById(Integer id);

    List<CommentDTO> findAll();

    PageResponse<CommentDTO> findByProduct(Integer productId, int page, int size);

    CommentDTO createComment(CommentDTO comment);

    MessageResponse deleteComment(Integer commentId);
}
