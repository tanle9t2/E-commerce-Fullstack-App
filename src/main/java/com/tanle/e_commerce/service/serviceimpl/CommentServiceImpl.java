package com.tanle.e_commerce.service.serviceimpl;

import com.tanle.e_commerce.Repository.Jpa.CommentRepository;
import com.tanle.e_commerce.entities.Comment;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.payload.MessageResponse;
import com.tanle.e_commerce.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public ResponseEntity<Comment> findById(Integer id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found Comment"));

        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Comment>> findAll() {
        List<Comment> comments = commentRepository.findAll();
        return new ResponseEntity<>(comments,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Comment>> findByProduct(Integer productId) {
        List<Comment> comments = commentRepository.getCommentByProduct(productId);
        return new ResponseEntity<>(comments,HttpStatus.OK);
    }
    @Override
    public ResponseEntity<List<Comment>> findByParentComment(Integer commnentId) {
        List<Comment> comments = commentRepository.getSubComment(commnentId);
        return new ResponseEntity<>(comments,HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Comment> createComment(Integer parentId, Comment comment) {
        Comment result = commentRepository.createComment(parentId,comment);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @Override
    @Transactional
    public MessageResponse deleteComment(Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found comment"));
        List<Comment> deletComment = commentRepository.getSubComment(commentId);
        deletComment.add(comment);
        commentRepository.updateIndexDelete(comment.getLeft(),comment.getRight());
        commentRepository.deleteAll(deletComment);
        return MessageResponse.builder()
                .message("Successfully delete comment")
                .status(HttpStatus.OK)
                .build();
    }

}
