package com.noglugo.mvp.service;

import com.noglugo.mvp.service.dto.OrderDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.noglugo.mvp.domain.Order}.
 */
public interface OrderService {
    /**
     * Save a order.
     *
     * @param orderDTO the entity to save.
     * @return the persisted entity.
     */
    OrderDTO save(OrderDTO orderDTO);

    /**
     * Updates a order.
     *
     * @param orderDTO the entity to update.
     * @return the persisted entity.
     */
    OrderDTO update(OrderDTO orderDTO);

    /**
     * Partially updates a order.
     *
     * @param orderDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrderDTO> partialUpdate(OrderDTO orderDTO);

    /**
     * Get all the orders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrderDTO> findAll(Pageable pageable);
    /**
     * Get all the OrderDTO where ShippingAddress is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<OrderDTO> findAllWhereShippingAddressIsNull();
    /**
     * Get all the OrderDTO where BillingAddress is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<OrderDTO> findAllWhereBillingAddressIsNull();

    /**
     * Get the "id" order.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrderDTO> findOne(UUID id);

    /**
     * Delete the "id" order.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    List<OrderDTO> findAllByUserId(Long userId);
}
