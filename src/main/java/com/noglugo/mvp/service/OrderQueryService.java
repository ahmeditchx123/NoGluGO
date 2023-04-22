package com.noglugo.mvp.service;

import com.noglugo.mvp.domain.*; // for static metamodels
import com.noglugo.mvp.domain.Order;
import com.noglugo.mvp.repository.OrderRepository;
import com.noglugo.mvp.service.criteria.OrderCriteria;
import com.noglugo.mvp.service.dto.OrderDTO;
import com.noglugo.mvp.service.mapper.OrderMapper;
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
 * Service for executing complex queries for {@link Order} entities in the database.
 * The main input is a {@link OrderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrderDTO} or a {@link Page} of {@link OrderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderQueryService extends QueryService<Order> {

    private final Logger log = LoggerFactory.getLogger(OrderQueryService.class);

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    public OrderQueryService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    /**
     * Return a {@link List} of {@link OrderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrderDTO> findByCriteria(OrderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Order> specification = createSpecification(criteria);
        return orderMapper.toDto(orderRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OrderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderDTO> findByCriteria(OrderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Order> specification = createSpecification(criteria);
        return orderRepository.findAll(specification, page).map(orderMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Order> specification = createSpecification(criteria);
        return orderRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Order> createSpecification(OrderCriteria criteria) {
        Specification<Order> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Order_.id));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), Order_.totalPrice));
            }
            if (criteria.getTotalItems() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalItems(), Order_.totalItems));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Order_.status));
            }
            if (criteria.getDeliveryMethod() != null) {
                specification = specification.and(buildSpecification(criteria.getDeliveryMethod(), Order_.deliveryMethod));
            }
            if (criteria.getPaymentMethod() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentMethod(), Order_.paymentMethod));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Order_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Order_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Order_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Order_.lastModifiedDate));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), Order_.userId));
            }
            if (criteria.getShippingAddressId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getShippingAddressId(),
                            root -> root.join(Order_.shippingAddress, JoinType.LEFT).get(Address_.id)
                        )
                    );
            }
            if (criteria.getBillingAddressId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBillingAddressId(),
                            root -> root.join(Order_.billingAddress, JoinType.LEFT).get(Address_.id)
                        )
                    );
            }
            if (criteria.getOrderItemsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrderItemsId(),
                            root -> root.join(Order_.orderItems, JoinType.LEFT).get(OrderItem_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
