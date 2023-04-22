package com.noglugo.mvp.service;

import com.noglugo.mvp.domain.*; // for static metamodels
import com.noglugo.mvp.domain.Product;
import com.noglugo.mvp.repository.ProductRepository;
import com.noglugo.mvp.service.criteria.ProductCriteria;
import com.noglugo.mvp.service.dto.ProductDTO;
import com.noglugo.mvp.service.mapper.ProductMapper;
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
 * Service for executing complex queries for {@link Product} entities in the database.
 * The main input is a {@link ProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductDTO} or a {@link Page} of {@link ProductDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductQueryService extends QueryService<Product> {

    private final Logger log = LoggerFactory.getLogger(ProductQueryService.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductQueryService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Return a {@link List} of {@link ProductDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> findByCriteria(ProductCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Product> specification = createSpecification(criteria);
        return productMapper.toDto(productRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductDTO> findByCriteria(ProductCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.findAll(specification, page).map(productMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Product> createSpecification(ProductCriteria criteria) {
        Specification<Product> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Product_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Product_.name));
            }
            if (criteria.getSku() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSku(), Product_.sku));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Product_.description));
            }
            if (criteria.getImgPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImgPath(), Product_.imgPath));
            }
            if (criteria.getUnitPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUnitPrice(), Product_.unitPrice));
            }
            if (criteria.getIsAvailable() != null) {
                specification = specification.and(buildSpecification(criteria.getIsAvailable(), Product_.isAvailable));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Product_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Product_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Product_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Product_.lastModifiedDate));
            }
            if (criteria.getCartItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCartItemId(), root -> root.join(Product_.cartItem, JoinType.LEFT).get(CartItem_.id))
                    );
            }
            if (criteria.getOrderItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrderItemId(),
                            root -> root.join(Product_.orderItem, JoinType.LEFT).get(OrderItem_.id)
                        )
                    );
            }
            if (criteria.getStoreId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getStoreId(), root -> root.join(Product_.store, JoinType.LEFT).get(Store_.id))
                    );
            }
            if (criteria.getInformationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInformationId(),
                            root -> root.join(Product_.information, JoinType.LEFT).get(ProductInfo_.id)
                        )
                    );
            }
            if (criteria.getReviewsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getReviewsId(), root -> root.join(Product_.reviews, JoinType.LEFT).get(Review_.id))
                    );
            }
        }
        return specification;
    }
}
