package com.noglugo.mvp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.noglugo.mvp.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class LocationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocationDTO.class);
        LocationDTO locationDTO1 = new LocationDTO();
        locationDTO1.setId(UUID.randomUUID());
        LocationDTO locationDTO2 = new LocationDTO();
        assertThat(locationDTO1).isNotEqualTo(locationDTO2);
        locationDTO2.setId(locationDTO1.getId());
        assertThat(locationDTO1).isEqualTo(locationDTO2);
        locationDTO2.setId(UUID.randomUUID());
        assertThat(locationDTO1).isNotEqualTo(locationDTO2);
        locationDTO1.setId(null);
        assertThat(locationDTO1).isNotEqualTo(locationDTO2);
    }
}
