package com.noglugo.mvp.service.mapper;

import com.noglugo.mvp.domain.Cart;
import com.noglugo.mvp.domain.CartItem;
import com.noglugo.mvp.service.dto.CartDTO;
import com.noglugo.mvp.service.dto.CartItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CartItem} and its DTO {@link CartItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface CartItemMapper extends EntityMapper<CartItemDTO, CartItem> {
    @Mapping(target = "cart", source = "cart", ignore = true)
    CartItemDTO toDto(CartItem s);

    @Named("cartId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CartDTO toDtoCartId(Cart cart);
}
