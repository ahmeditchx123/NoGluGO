package com.noglugo.mvp.service;

import com.noglugo.mvp.domain.*; // for static metamodels
import com.noglugo.mvp.domain.Review;
import com.noglugo.mvp.repository.ReviewRepository;
import com.noglugo.mvp.service.criteria.ReviewCriteria;
import com.noglugo.mvp.service.dto.ReviewDTO;
import com.noglugo.mvp.service.mapper.ReviewMapper;
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
 * Service for executing complex queries for {@link Review} entities in the database.
 * The main input is a {@link ReviewCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReviewDTO} or a {@link Page} of {@link ReviewDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReviewQueryService extends QueryService<Review> {

    private final Logger log = LoggerFactory.getLogger(ReviewQueryService.class);

    private final ReviewRepository reviewRepository;

    private final ReviewMapper reviewMapper;

    public ReviewQueryService(ReviewRepository reviewRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
    }

    /**
     * Return a {@link List} of {@link ReviewDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReviewDTO> findByCriteria(ReviewCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Review> specification = createSpecification(criteria);
        return reviewMapper.toDto(reviewRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ReviewDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReviewDTO> findByCriteria(ReviewCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Review> specification = createSpecification(criteria);
        return reviewRepository.findAll(specification, page).map(reviewMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReviewCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Review> specification = createSpecification(criteria);
        return reviewRepository.count(specification);
    }

    /**
     * Function to convert {@link ReviewCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Review> createSpecification(ReviewCriteria criteria) {
        Specification<Review> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Review_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Review_.title));
            }
            if (criteria.getContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContent(), Review_.content));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), Review_.rating));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Review_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Review_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Review_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Review_.lastModifiedDate));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), Review_.userId));
            }
            if (criteria.getMenuItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMenuItemId(), root -> root.join(Review_.menuItem, JoinType.LEFT).get(MenuItem_.id))
                    );
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProductId(), root -> root.join(Review_.product, JoinType.LEFT).get(Product_.id))
                    );
            }
        }
        return specification;
    }
}
