package com.noglugo.mvp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.noglugo.mvp.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ReviewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Review.class);
        Review review1 = new Review();
        review1.setId(UUID.randomUUID());
        Review review2 = new Review();
        review2.setId(review1.getId());
        assertThat(review1).isEqualTo(review2);
        review2.setId(UUID.randomUUID());
        assertThat(review1).isNotEqualTo(review2);
        review1.setId(null);
        assertThat(review1).isNotEqualTo(review2);
    }
}
