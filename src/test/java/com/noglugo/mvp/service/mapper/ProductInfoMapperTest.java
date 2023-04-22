package com.noglugo.mvp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductInfoMapperTest {

    private ProductInfoMapper productInfoMapper;

    @BeforeEach
    public void setUp() {
        productInfoMapper = new ProductInfoMapperImpl();
    }
}
