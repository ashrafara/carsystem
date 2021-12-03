package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DispatchTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dispatch.class);
        Dispatch dispatch1 = new Dispatch();
        dispatch1.setId(1L);
        Dispatch dispatch2 = new Dispatch();
        dispatch2.setId(dispatch1.getId());
        assertThat(dispatch1).isEqualTo(dispatch2);
        dispatch2.setId(2L);
        assertThat(dispatch1).isNotEqualTo(dispatch2);
        dispatch1.setId(null);
        assertThat(dispatch1).isNotEqualTo(dispatch2);
    }
}
