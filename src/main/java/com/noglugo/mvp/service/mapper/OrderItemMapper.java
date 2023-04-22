package com.noglugo.mvp.service.mapper;

import com.noglugo.mvp.domain.Order;
import com.noglugo.mvp.domain.OrderItem;
import com.noglugo.mvp.service.dto.OrderDTO;
import com.noglugo.mvp.service.dto.OrderItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderItem} and its DTO {@link OrderItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderItemMapper extends EntityMapper<OrderItemDTO, OrderItem> {
    @Mapping(target = "order", source = "order", ignore = true)
    OrderItemDTO toDto(OrderItem s);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoOrderId(Order order);
}
