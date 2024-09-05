package com.zb.fresh_api.domain.repository.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import com.zb.fresh_api.domain.entity.terms.Terms;
import com.zb.fresh_api.domain.enums.terms.TermsType;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TermsJpaRepositoryTest {

    @Autowired
    private TermsJpaRepository termsJpaRepository;

    @Test
    @DisplayName("필수항목인 약관 조회 성공")
    void findAllByIsRequired_success() {
        // given
        Terms terms1 = Terms.builder().title("title1").content("content1").type(TermsType.SERVICE)
            .isUsed(true).isRequired(true).build();
        Terms terms2 = Terms.builder().title("title2").content("content2").type(TermsType.PRIVACY)
            .isUsed(true).isRequired(true).build();
        Terms terms3 = Terms.builder().title("title3").content("content3").type(TermsType.MARKETING)
            .isUsed(true).isRequired(false).build();

        // when
        termsJpaRepository.save(terms1);
        termsJpaRepository.save(terms2);
        termsJpaRepository.save(terms3);
        List<Terms> terms = termsJpaRepository.findAllByIsRequired(true);
        // then
        assertThat(terms.size()).isNotEqualTo(3);
    }
}
