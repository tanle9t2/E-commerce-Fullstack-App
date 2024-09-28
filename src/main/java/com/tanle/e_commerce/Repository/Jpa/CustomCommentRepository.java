package com.tanle.e_commerce.Repository.Jpa;

import com.tanle.e_commerce.entities.Comment;

public interface CustomCommentRepository {
    Comment createComment(Integer parentId, Comment comment);
    void updateIndexDelete(int left, int right);
}
