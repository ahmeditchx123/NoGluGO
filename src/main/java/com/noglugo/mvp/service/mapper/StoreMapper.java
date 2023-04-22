package com.noglugo.mvp.service.mapper;

import com.noglugo.mvp.domain.Restaurant;
import com.noglugo.mvp.domain.Store;
import com.noglugo.mvp.service.dto.RestaurantDTO;
import com.noglugo.mvp.service.dto.StoreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Store} and its DTO {@link StoreDTO}.
 */
@Mapper(componentModel = "spring", uses = { AddressMapper.class })
public interface StoreMapper extends EntityMapper<StoreDTO, Store> {
    @Mapping(target = "addressDTO", source = "storeAddress")
    StoreDTO toDto(Store s);
}
