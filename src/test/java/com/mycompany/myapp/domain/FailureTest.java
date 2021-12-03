package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FailureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Failure.class);
        Failure failure1 = new Failure();
        failure1.setId(1L);
        Failure failure2 = new Failure();
        failure2.setId(failure1.getId());
        assertThat(failure1).isEqualTo(failure2);
        failure2.setId(2L);
        assertThat(failure1).isNotEqualTo(failure2);
        failure1.setId(null);
        assertThat(failure1).isNotEqualTo(failure2);
    }
}
