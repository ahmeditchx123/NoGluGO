package com.noglugo.mvp.service.mapper;

import com.noglugo.mvp.domain.MenuItem;
import com.noglugo.mvp.domain.Product;
import com.noglugo.mvp.domain.Review;
import com.noglugo.mvp.service.dto.MenuItemDTO;
import com.noglugo.mvp.service.dto.ProductDTO;
import com.noglugo.mvp.service.dto.ReviewDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Review} and its DTO {@link ReviewDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReviewMapper extends EntityMapper<ReviewDTO, Review> {
    @Mapping(target = "menuItem", source = "menuItem", qualifiedByName = "menuItemId")
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    ReviewDTO toDto(Review s);

    @Named("menuItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MenuItemDTO toDtoMenuItemId(MenuItem menuItem);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);
}
