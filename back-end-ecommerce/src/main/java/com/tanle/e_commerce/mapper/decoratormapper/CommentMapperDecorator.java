package com.tanle.e_commerce.mapper.decoratormapper;

import com.tanle.e_commerce.dto.CommentDTO;
import com.tanle.e_commerce.entities.Comment;
import com.tanle.e_commerce.mapper.CommentMapper;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@NoArgsConstructor
@Mapper
public abstract class CommentMapperDecorator implements CommentMapper {
    @Autowired
    private CommentMapper deligate;

    @Override
    public CommentDTO convertDto(Comment comment) {
        CommentDTO result = deligate.convertDto(comment);
        result.setProductId(comment.getSku().getProduct().getId());
        result.setSkuName(comment.getSku().getModalName());
        result.setUserInfor(
                result.newUserInfor(comment.getUser().getFullName(), comment.getUser().getAvtUrl())
        );
        return result;
    }
}
