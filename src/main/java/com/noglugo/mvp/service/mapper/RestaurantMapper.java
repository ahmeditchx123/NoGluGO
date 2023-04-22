package com.noglugo.mvp.service.mapper;

import com.noglugo.mvp.domain.MenuItem;
import com.noglugo.mvp.domain.Restaurant;
import com.noglugo.mvp.service.dto.MenuItemDTO;
import com.noglugo.mvp.service.dto.RestaurantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Restaurant} and its DTO {@link RestaurantDTO}.
 */
@Mapper(componentModel = "spring", uses = { MenuMapper.class, AddressMapper.class })
public interface RestaurantMapper extends EntityMapper<RestaurantDTO, Restaurant> {
    @Mapping(target = "menuDTO", source = "restaurantMenu")
    @Mapping(target = "addressDTO", source = "restaurantAddress")
    RestaurantDTO toDto(Restaurant r);
}
