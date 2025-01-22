package com.tanle.e_commerce.mapper;

import com.tanle.e_commerce.dto.CommentDTO;
import com.tanle.e_commerce.entities.Comment;
import com.tanle.e_commerce.mapper.decoratormapper.CommentMapperDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
@DecoratedWith(CommentMapperDecorator.class)
public interface CommentMapper {
    CommentDTO convertDto(Comment comment);

    Comment convertEntity(CommentDTO commentDTO);
}
