package com.noglugo.mvp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.noglugo.mvp.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ArticleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Article.class);
        Article article1 = new Article();
        article1.setId(UUID.randomUUID());
        Article article2 = new Article();
        article2.setId(article1.getId());
        assertThat(article1).isEqualTo(article2);
        article2.setId(UUID.randomUUID());
        assertThat(article1).isNotEqualTo(article2);
        article1.setId(null);
        assertThat(article1).isNotEqualTo(article2);
    }
}
