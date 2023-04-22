package com.noglugo.mvp.service.mapper;

import com.noglugo.mvp.domain.Address;
import com.noglugo.mvp.domain.Order;
import com.noglugo.mvp.domain.Restaurant;
import com.noglugo.mvp.domain.Store;
import com.noglugo.mvp.service.dto.AddressDTO;
import com.noglugo.mvp.service.dto.OrderDTO;
import com.noglugo.mvp.service.dto.RestaurantDTO;
import com.noglugo.mvp.service.dto.StoreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Address} and its DTO {@link AddressDTO}.
 */
@Mapper(componentModel = "spring", uses = { LocationMapper.class })
public interface AddressMapper extends EntityMapper<AddressDTO, Address> {
    @Mapping(target = "orderSA", source = "orderSA", qualifiedByName = "orderId")
    @Mapping(target = "orderBA", source = "orderBA", qualifiedByName = "orderId")
    @Mapping(target = "store", source = "store", ignore = true)
    @Mapping(target = "restaurant", source = "restaurant", qualifiedByName = "restaurantId")
    @Mapping(target = "locationDTO", source = "location")
    AddressDTO toDto(Address s);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoOrderId(Order order);

    @Named("storeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StoreDTO toDtoStoreId(Store store);

    @Named("restaurantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RestaurantDTO toDtoRestaurantId(Restaurant restaurant);
}
