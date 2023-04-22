package com.noglugo.mvp.service.mapper;

import com.noglugo.mvp.domain.Menu;
import com.noglugo.mvp.domain.MenuItem;
import com.noglugo.mvp.service.dto.MenuDTO;
import com.noglugo.mvp.service.dto.MenuItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MenuItem} and its DTO {@link MenuItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface MenuItemMapper extends EntityMapper<MenuItemDTO, MenuItem> {
    @Mapping(target = "menu", ignore = true)
    MenuItemDTO toDto(MenuItem s);
}
