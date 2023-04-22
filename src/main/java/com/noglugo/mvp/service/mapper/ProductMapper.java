package com.noglugo.mvp.service.mapper;

import com.noglugo.mvp.domain.CartItem;
import com.noglugo.mvp.domain.OrderItem;
import com.noglugo.mvp.domain.Product;
import com.noglugo.mvp.domain.Store;
import com.noglugo.mvp.service.dto.CartItemDTO;
import com.noglugo.mvp.service.dto.OrderItemDTO;
import com.noglugo.mvp.service.dto.ProductDTO;
import com.noglugo.mvp.service.dto.StoreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductInfoMapper.class })
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "productInfoDTO", source = "information")
    ProductDTO toDto(Product s);
}
