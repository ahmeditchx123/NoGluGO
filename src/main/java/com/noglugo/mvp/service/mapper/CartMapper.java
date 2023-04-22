package com.noglugo.mvp.service.mapper;

import com.noglugo.mvp.domain.Cart;
import com.noglugo.mvp.service.dto.CartDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cart} and its DTO {@link CartDTO}.
 */
@Mapper(componentModel = "spring", uses = { CartItemMapper.class })
public interface CartMapper extends EntityMapper<CartDTO, Cart> {
    @Mapping(target = "cartItemDTOS", source = "cartItems")
    CartDTO toDto(Cart cart);
}
