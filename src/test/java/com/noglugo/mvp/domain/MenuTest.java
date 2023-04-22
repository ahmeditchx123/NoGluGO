package com.noglugo.mvp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.noglugo.mvp.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Menu.class);
        Menu menu1 = new Menu();
        menu1.setId(UUID.randomUUID());
        Menu menu2 = new Menu();
        menu2.setId(menu1.getId());
        assertThat(menu1).isEqualTo(menu2);
        menu2.setId(UUID.randomUUID());
        assertThat(menu1).isNotEqualTo(menu2);
        menu1.setId(null);
        assertThat(menu1).isNotEqualTo(menu2);
    }
}
