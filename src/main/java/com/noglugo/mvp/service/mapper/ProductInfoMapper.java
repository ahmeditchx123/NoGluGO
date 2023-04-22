package com.noglugo.mvp.service.mapper;

import com.noglugo.mvp.domain.Product;
import com.noglugo.mvp.domain.ProductInfo;
import com.noglugo.mvp.service.dto.ProductDTO;
import com.noglugo.mvp.service.dto.ProductInfoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductInfo} and its DTO {@link ProductInfoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductInfoMapper extends EntityMapper<ProductInfoDTO, ProductInfo> {
    @Mapping(target = "product", ignore = true)
    ProductInfoDTO toDto(ProductInfo s);
}
