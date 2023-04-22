package com.noglugo.mvp.service.mapper;

import com.noglugo.mvp.domain.Order;
import com.noglugo.mvp.service.dto.OrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring", uses = { OrderItemMapper.class })
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {
    @Mapping(target = "orderItemDTOs", source = "orderItems")
    OrderDTO toDto(Order order);
}
