package com.noglugo.mvp.service.mapper;

import com.noglugo.mvp.domain.Menu;
import com.noglugo.mvp.domain.Restaurant;
import com.noglugo.mvp.service.dto.MenuDTO;
import com.noglugo.mvp.service.dto.RestaurantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Menu} and its DTO {@link MenuDTO}.
 */
@Mapper(componentModel = "spring", uses = { MenuItemMapper.class })
public interface MenuMapper extends EntityMapper<MenuDTO, Menu> {
    @Mapping(target = "restaurant", source = "restaurant", qualifiedByName = "restaurantId")
    @Mapping(target = "menuItemDTOS", source = "menuItems")
    MenuDTO toDto(Menu s);

    @Named("restaurantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RestaurantDTO toDtoRestaurantId(Restaurant restaurant);
}
