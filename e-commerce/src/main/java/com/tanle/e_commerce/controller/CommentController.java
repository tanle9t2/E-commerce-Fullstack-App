package com.tanle.e_commerce.controller;

import com.tanle.e_commerce.dto.CommentDTO;
import com.tanle.e_commerce.entities.Comment;
import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.respone.PageResponse;
import com.tanle.e_commerce.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.tanle.e_commerce.utils.AppConstant.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:5173") // React app URL
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/comment/{commentId}")
    public ResponseEntity<CommentDTO> getComment(@PathVariable Integer commentId) {
        CommentDTO commentDTO = commentService.findById(commentId);
        return ResponseEntity.ok(commentDTO);
    }

    @GetMapping("/comment/product/{productId}")
    public ResponseEntity<PageResponse<CommentDTO>> getCommentByProduct(
            @PathVariable Integer productId
            , @RequestParam(value = "page", defaultValue = PAGE_DEFAULT) String page
            , @RequestParam(value = "size", defaultValue = PAGE_SIZE_COMMENT) String size) {
        PageResponse commentDTO = commentService.findByProduct(productId, Integer.parseInt(page)-1, Integer.parseInt(size));
        return ResponseEntity.ok(commentDTO);
    }


    @GetMapping("/comments")
    public ResponseEntity<List<CommentDTO>> getComment() {
        List<CommentDTO> commentDTOS = commentService.findAll();
        return ResponseEntity.ok(commentDTOS);
    }


    @PostMapping("/comment")
    public ResponseEntity<CommentDTO> createComment(
            @RequestBody CommentDTO comment
    ) {
        CommentDTO result = commentService.createComment(comment);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/comment/{commentId}")
    public MessageResponse deleteComment(@PathVariable int commentId) {
        return commentService.deleteComment(commentId);
    }
}
