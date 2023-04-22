package com.noglugo.mvp.service.mapper;

import com.noglugo.mvp.domain.Article;
import com.noglugo.mvp.domain.Comment;
import com.noglugo.mvp.service.dto.ArticleDTO;
import com.noglugo.mvp.service.dto.CommentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Comment} and its DTO {@link CommentDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommentMapper extends EntityMapper<CommentDTO, Comment> {
    @Mapping(target = "article", source = "article", qualifiedByName = "articleId")
    CommentDTO toDto(Comment s);

    @Named("articleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ArticleDTO toDtoArticleId(Article article);
}
