package com.noglugo.mvp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.noglugo.mvp.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ProductInfoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductInfo.class);
        ProductInfo productInfo1 = new ProductInfo();
        productInfo1.setId(UUID.randomUUID());
        ProductInfo productInfo2 = new ProductInfo();
        productInfo2.setId(productInfo1.getId());
        assertThat(productInfo1).isEqualTo(productInfo2);
        productInfo2.setId(UUID.randomUUID());
        assertThat(productInfo1).isNotEqualTo(productInfo2);
        productInfo1.setId(null);
        assertThat(productInfo1).isNotEqualTo(productInfo2);
    }
}
