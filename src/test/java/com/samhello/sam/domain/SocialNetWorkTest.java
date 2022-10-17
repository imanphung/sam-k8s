package com.samhello.sam.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.samhello.sam.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SocialNetWorkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SocialNetWork.class);
        SocialNetWork socialNetWork1 = new SocialNetWork();
        socialNetWork1.setId(1L);
        SocialNetWork socialNetWork2 = new SocialNetWork();
        socialNetWork2.setId(socialNetWork1.getId());
        assertThat(socialNetWork1).isEqualTo(socialNetWork2);
        socialNetWork2.setId(2L);
        assertThat(socialNetWork1).isNotEqualTo(socialNetWork2);
        socialNetWork1.setId(null);
        assertThat(socialNetWork1).isNotEqualTo(socialNetWork2);
    }
}
