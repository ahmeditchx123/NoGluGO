package com.noglugo.mvp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.noglugo.mvp.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class GlutenProfileDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GlutenProfileDTO.class);
        GlutenProfileDTO glutenProfileDTO1 = new GlutenProfileDTO();
        glutenProfileDTO1.setId(UUID.randomUUID());
        GlutenProfileDTO glutenProfileDTO2 = new GlutenProfileDTO();
        assertThat(glutenProfileDTO1).isNotEqualTo(glutenProfileDTO2);
        glutenProfileDTO2.setId(glutenProfileDTO1.getId());
        assertThat(glutenProfileDTO1).isEqualTo(glutenProfileDTO2);
        glutenProfileDTO2.setId(UUID.randomUUID());
        assertThat(glutenProfileDTO1).isNotEqualTo(glutenProfileDTO2);
        glutenProfileDTO1.setId(null);
        assertThat(glutenProfileDTO1).isNotEqualTo(glutenProfileDTO2);
    }
}
