package com.noglugo.mvp.service;

import com.noglugo.mvp.domain.*; // for static metamodels
import com.noglugo.mvp.domain.Article;
import com.noglugo.mvp.repository.ArticleRepository;
import com.noglugo.mvp.service.criteria.ArticleCriteria;
import com.noglugo.mvp.service.dto.ArticleDTO;
import com.noglugo.mvp.service.mapper.ArticleMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Article} entities in the database.
 * The main input is a {@link ArticleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ArticleDTO} or a {@link Page} of {@link ArticleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ArticleQueryService extends QueryService<Article> {

    private final Logger log = LoggerFactory.getLogger(ArticleQueryService.class);

    private final ArticleRepository articleRepository;

    private final ArticleMapper articleMapper;

    public ArticleQueryService(ArticleRepository articleRepository, ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
    }

    /**
     * Return a {@link List} of {@link ArticleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ArticleDTO> findByCriteria(ArticleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Article> specification = createSpecification(criteria);
        return articleMapper.toDto(articleRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ArticleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ArticleDTO> findByCriteria(ArticleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Article> specification = createSpecification(criteria);
        return articleRepository.findAll(specification, page).map(articleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ArticleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Article> specification = createSpecification(criteria);
        return articleRepository.count(specification);
    }

    /**
     * Function to convert {@link ArticleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Article> createSpecification(ArticleCriteria criteria) {
        Specification<Article> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Article_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Article_.name));
            }
            if (criteria.getContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContent(), Article_.content));
            }
            if (criteria.getImgPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImgPath(), Article_.imgPath));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Article_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Article_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Article_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Article_.lastModifiedDate));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), Article_.userId));
            }
            if (criteria.getCommentsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCommentsId(), root -> root.join(Article_.comments, JoinType.LEFT).get(Comment_.id))
                    );
            }
        }
        return specification;
    }
}
