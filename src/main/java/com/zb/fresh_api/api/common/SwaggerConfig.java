package com.zb.fresh_api.api.common;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;

public record SwaggerConfig() {
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("fresh-api")
                .pathsToMatch("/api/**")
                .build();
    }
}
