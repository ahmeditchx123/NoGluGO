package com.noglugo.mvp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.noglugo.mvp.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class GlutenProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GlutenProfile.class);
        GlutenProfile glutenProfile1 = new GlutenProfile();
        glutenProfile1.setId(UUID.randomUUID());
        GlutenProfile glutenProfile2 = new GlutenProfile();
        glutenProfile2.setId(glutenProfile1.getId());
        assertThat(glutenProfile1).isEqualTo(glutenProfile2);
        glutenProfile2.setId(UUID.randomUUID());
        assertThat(glutenProfile1).isNotEqualTo(glutenProfile2);
        glutenProfile1.setId(null);
        assertThat(glutenProfile1).isNotEqualTo(glutenProfile2);
    }
}
