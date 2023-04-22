package com.noglugo.mvp.service.mapper;

import com.noglugo.mvp.domain.Article;
import com.noglugo.mvp.service.dto.ArticleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Article} and its DTO {@link ArticleDTO}.
 */
@Mapper(componentModel = "spring")
public interface ArticleMapper extends EntityMapper<ArticleDTO, Article> {}
