package com.noglugo.mvp.service.mapper;

import com.noglugo.mvp.domain.Address;
import com.noglugo.mvp.domain.Location;
import com.noglugo.mvp.service.dto.AddressDTO;
import com.noglugo.mvp.service.dto.LocationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Location} and its DTO {@link LocationDTO}.
 */
@Mapper(componentModel = "spring")
public interface LocationMapper extends EntityMapper<LocationDTO, Location> {
    @Mapping(target = "address", source = "address", ignore = true)
    LocationDTO toDto(Location s);

    @Named("addressId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AddressDTO toDtoAddressId(Address address);
}
