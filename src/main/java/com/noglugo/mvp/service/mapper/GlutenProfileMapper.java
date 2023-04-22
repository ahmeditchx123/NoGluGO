package com.noglugo.mvp.service.mapper;

import com.noglugo.mvp.domain.GlutenProfile;
import com.noglugo.mvp.service.dto.GlutenProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GlutenProfile} and its DTO {@link GlutenProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface GlutenProfileMapper extends EntityMapper<GlutenProfileDTO, GlutenProfile> {}
