package com.tanle.e_commerce.service.serviceimpl;

import com.tanle.e_commerce.Repository.Jpa.CommentRepository;
import com.tanle.e_commerce.dto.CommentDTO;
import com.tanle.e_commerce.entities.Comment;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.mapper.CommentMapper;
import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.respone.PageResponse;
import com.tanle.e_commerce.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public CommentDTO findById(Integer id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found Comment"));

        return commentMapper.convertDto(comment);
    }

    @Override
    public List<CommentDTO> findAll() {
        List<CommentDTO> comments = commentRepository.findAll()
                .stream()
                .map(comment -> commentMapper.convertDto(comment))
                .collect(Collectors.toList());
        return comments;
    }

    @Override
    public PageResponse<CommentDTO> findByProduct(Integer productId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> comments = commentRepository.getCommentByProduct(productId, pageable);
        PageResponse result = PageResponse.builder()
                .data(comments.getContent().stream()
                        .map(comment -> commentMapper.convertDto(comment))
                        .collect(Collectors.toList()))
                .totalElement(comments.getNumberOfElements())
                .page(comments.getNumber())
                .count(comments.getTotalElements())
                .build();
        return result;
    }

    @Override
    @Transactional
    public CommentDTO createComment(CommentDTO comment) {
        Comment result = commentRepository.save(commentMapper.convertEntity(comment));
        return commentMapper.convertDto(result);
    }

    @Override
    @Transactional
    public MessageResponse deleteComment(Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found comment"));
        commentRepository.delete(comment);
        return MessageResponse.builder()
                .message("Successfully delete comment")
                .status(HttpStatus.OK)
                .build();
    }

}
