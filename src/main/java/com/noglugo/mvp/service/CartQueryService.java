package com.noglugo.mvp.service;

import com.noglugo.mvp.domain.*; // for static metamodels
import com.noglugo.mvp.domain.Cart;
import com.noglugo.mvp.repository.CartRepository;
import com.noglugo.mvp.service.criteria.CartCriteria;
import com.noglugo.mvp.service.dto.CartDTO;
import com.noglugo.mvp.service.mapper.CartMapper;
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
 * Service for executing complex queries for {@link Cart} entities in the database.
 * The main input is a {@link CartCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CartDTO} or a {@link Page} of {@link CartDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CartQueryService extends QueryService<Cart> {

    private final Logger log = LoggerFactory.getLogger(CartQueryService.class);

    private final CartRepository cartRepository;

    private final CartMapper cartMapper;

    public CartQueryService(CartRepository cartRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    /**
     * Return a {@link List} of {@link CartDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CartDTO> findByCriteria(CartCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Cart> specification = createSpecification(criteria);
        return cartMapper.toDto(cartRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CartDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CartDTO> findByCriteria(CartCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cart> specification = createSpecification(criteria);
        return cartRepository.findAll(specification, page).map(cartMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CartCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Cart> specification = createSpecification(criteria);
        return cartRepository.count(specification);
    }

    /**
     * Function to convert {@link CartCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cart> createSpecification(CartCriteria criteria) {
        Specification<Cart> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Cart_.id));
            }
            if (criteria.getTotalItems() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalItems(), Cart_.totalItems));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), Cart_.totalPrice));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Cart_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Cart_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Cart_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Cart_.lastModifiedDate));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), Cart_.userId));
            }
            if (criteria.getCartItemsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCartItemsId(), root -> root.join(Cart_.cartItems, JoinType.LEFT).get(CartItem_.id))
                    );
            }
        }
        return specification;
    }
}
