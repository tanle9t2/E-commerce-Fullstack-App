package com.tanle.e_commerce.controller;

import com.tanle.e_commerce.entities.Comment;
import com.tanle.e_commerce.payload.MessageResponse;
import com.tanle.e_commerce.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/comment/{commentId}")
    public ResponseEntity<Comment> getComment(@PathVariable Integer commentId) {
        return commentService.findById(commentId);
    }
    @GetMapping("/comments")
    public ResponseEntity<List<Comment>> getComment() {
        return commentService.findAll();
    }

    @GetMapping("/comment")
    public ResponseEntity<List<Comment>> getSubComent(@RequestParam("root")String parentId) {
        return commentService.findByParentComment(Integer.parseInt(parentId));
    }
    @PostMapping("/comment")
    public ResponseEntity<Comment> createComment(
            @RequestBody Comment comment,
            @RequestParam("commentid") String parentId) {
        return commentService.createComment(Integer.parseInt(parentId),comment);
    }
    @DeleteMapping("/comment/{commentId}")
    public MessageResponse deleteComment(@PathVariable int commentId) {
        return commentService.deleteComment(commentId);
    }
}
