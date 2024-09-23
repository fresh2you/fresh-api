package com.zb.fresh_api.common.base;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;
import com.zb.fresh_api.domain.config.JpaAuditingConfig;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.enums.member.Provider;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

/**
 * @Import : 클래스의 설정을 테스트 환경
 * @ActiveProfiles : test 환경 구축 필요
 */
@Import(JpaAuditingConfig.class)
@ActiveProfiles("local")
@ExtendWith(MockitoExtension.class)
public abstract class ServiceTest {

    protected Member getMember() {
        return getReflectionMonkey().giveMeBuilder(Member.class)
                .set("id", Arbitraries.longs().greaterOrEqual(4L))
                .set("provider", Arbitraries.of(Provider.class))
                .set("providerId", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(50))
                .sample();
    }

    protected final FixtureMonkey getReflectionMonkey() {
        return FixtureMonkey.builder()
                .plugin(new JakartaValidationPlugin())
                .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
                .build();
    }

    protected final FixtureMonkey getConstructorMonkey() {
        return FixtureMonkey.builder()
                .plugin(new JakartaValidationPlugin())
                .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
                .build();
    }

    protected final FixtureMonkey getBuilderMonkey() {
        return FixtureMonkey.builder()
                .plugin(new JakartaValidationPlugin())
                .objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
                .build();
    }

}
