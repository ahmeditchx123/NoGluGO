package com.noglugo.mvp.service.impl;

import com.noglugo.mvp.domain.Order;
import com.noglugo.mvp.repository.OrderRepository;
import com.noglugo.mvp.service.OrderService;
import com.noglugo.mvp.service.dto.OrderDTO;
import com.noglugo.mvp.service.mapper.OrderMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Order}.
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public OrderDTO save(OrderDTO orderDTO) {
        log.debug("Request to save Order : {}", orderDTO);
        Order order = orderMapper.toEntity(orderDTO);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDTO update(OrderDTO orderDTO) {
        log.debug("Request to update Order : {}", orderDTO);
        Order order = orderMapper.toEntity(orderDTO);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public Optional<OrderDTO> partialUpdate(OrderDTO orderDTO) {
        log.debug("Request to partially update Order : {}", orderDTO);

        return orderRepository
            .findById(orderDTO.getId())
            .map(existingOrder -> {
                orderMapper.partialUpdate(existingOrder, orderDTO);

                return existingOrder;
            })
            .map(orderRepository::save)
            .map(orderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Orders");
        return orderRepository.findAll(pageable).map(orderMapper::toDto);
    }

    /**
     * Get all the orders where ShippingAddress is {@code null}.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrderDTO> findAllWhereShippingAddressIsNull() {
        log.debug("Request to get all orders where ShippingAddress is null");
        return StreamSupport
            .stream(orderRepository.findAll().spliterator(), false)
            .filter(order -> order.getShippingAddress() == null)
            .map(orderMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the orders where BillingAddress is {@code null}.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrderDTO> findAllWhereBillingAddressIsNull() {
        log.debug("Request to get all orders where BillingAddress is null");
        return StreamSupport
            .stream(orderRepository.findAll().spliterator(), false)
            .filter(order -> order.getBillingAddress() == null)
            .map(orderMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderDTO> findOne(UUID id) {
        log.debug("Request to get Order : {}", id);
        return orderRepository.findById(id).map(orderMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Order : {}", id);
        orderRepository.deleteById(id);
    }

    @Override
    public List<OrderDTO> findAllByUserId(Long userId) {
        return orderRepository.findAllByUserIdEquals(userId).stream().map(orderMapper::toDto).collect(Collectors.toList());
    }
}
