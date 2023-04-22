package com.noglugo.mvp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.noglugo.mvp.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ProductInfoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductInfoDTO.class);
        ProductInfoDTO productInfoDTO1 = new ProductInfoDTO();
        productInfoDTO1.setId(UUID.randomUUID());
        ProductInfoDTO productInfoDTO2 = new ProductInfoDTO();
        assertThat(productInfoDTO1).isNotEqualTo(productInfoDTO2);
        productInfoDTO2.setId(productInfoDTO1.getId());
        assertThat(productInfoDTO1).isEqualTo(productInfoDTO2);
        productInfoDTO2.setId(UUID.randomUUID());
        assertThat(productInfoDTO1).isNotEqualTo(productInfoDTO2);
        productInfoDTO1.setId(null);
        assertThat(productInfoDTO1).isNotEqualTo(productInfoDTO2);
    }
}
