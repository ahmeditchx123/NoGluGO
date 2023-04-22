package com.noglugo.mvp.service;

import com.noglugo.mvp.domain.*; // for static metamodels
import com.noglugo.mvp.domain.OrderItem;
import com.noglugo.mvp.repository.OrderItemRepository;
import com.noglugo.mvp.service.criteria.OrderItemCriteria;
import com.noglugo.mvp.service.dto.OrderItemDTO;
import com.noglugo.mvp.service.mapper.OrderItemMapper;
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
 * Service for executing complex queries for {@link OrderItem} entities in the database.
 * The main input is a {@link OrderItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrderItemDTO} or a {@link Page} of {@link OrderItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderItemQueryService extends QueryService<OrderItem> {

    private final Logger log = LoggerFactory.getLogger(OrderItemQueryService.class);

    private final OrderItemRepository orderItemRepository;

    private final OrderItemMapper orderItemMapper;

    public OrderItemQueryService(OrderItemRepository orderItemRepository, OrderItemMapper orderItemMapper) {
        this.orderItemRepository = orderItemRepository;
        this.orderItemMapper = orderItemMapper;
    }

    /**
     * Return a {@link List} of {@link OrderItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrderItemDTO> findByCriteria(OrderItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrderItem> specification = createSpecification(criteria);
        return orderItemMapper.toDto(orderItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OrderItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderItemDTO> findByCriteria(OrderItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderItem> specification = createSpecification(criteria);
        return orderItemRepository.findAll(specification, page).map(orderItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OrderItem> specification = createSpecification(criteria);
        return orderItemRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderItem> createSpecification(OrderItemCriteria criteria) {
        Specification<OrderItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), OrderItem_.id));
            }
            if (criteria.getQty() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQty(), OrderItem_.qty));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), OrderItem_.totalPrice));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), OrderItem_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), OrderItem_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), OrderItem_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), OrderItem_.lastModifiedDate));
            }
            if (criteria.getOrderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOrderId(), root -> root.join(OrderItem_.order, JoinType.LEFT).get(Order_.id))
                    );
            }
            if (criteria.getProductsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProductsId(), root -> root.join(OrderItem_.products, JoinType.LEFT).get(Product_.id))
                    );
            }
        }
        return specification;
    }
}
