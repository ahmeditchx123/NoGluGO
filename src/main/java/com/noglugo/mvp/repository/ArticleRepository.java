package com.noglugo.mvp.repository;

import com.noglugo.mvp.domain.Article;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Article entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID>, JpaSpecificationExecutor<Article> {}
