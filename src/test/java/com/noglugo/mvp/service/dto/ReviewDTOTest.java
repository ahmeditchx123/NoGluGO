package com.noglugo.mvp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.noglugo.mvp.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ReviewDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewDTO.class);
        ReviewDTO reviewDTO1 = new ReviewDTO();
        reviewDTO1.setId(UUID.randomUUID());
        ReviewDTO reviewDTO2 = new ReviewDTO();
        assertThat(reviewDTO1).isNotEqualTo(reviewDTO2);
        reviewDTO2.setId(reviewDTO1.getId());
        assertThat(reviewDTO1).isEqualTo(reviewDTO2);
        reviewDTO2.setId(UUID.randomUUID());
        assertThat(reviewDTO1).isNotEqualTo(reviewDTO2);
        reviewDTO1.setId(null);
        assertThat(reviewDTO1).isNotEqualTo(reviewDTO2);
    }
}
