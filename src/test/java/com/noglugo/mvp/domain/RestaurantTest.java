package com.noglugo.mvp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.noglugo.mvp.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RestaurantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Restaurant.class);
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setId(UUID.randomUUID());
        Restaurant restaurant2 = new Restaurant();
        restaurant2.setId(restaurant1.getId());
        assertThat(restaurant1).isEqualTo(restaurant2);
        restaurant2.setId(UUID.randomUUID());
        assertThat(restaurant1).isNotEqualTo(restaurant2);
        restaurant1.setId(null);
        assertThat(restaurant1).isNotEqualTo(restaurant2);
    }
}
