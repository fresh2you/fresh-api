package com.zb.fresh_api.api.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {"com.zb.fresh_api.api"})
public class FeignClientConfig {
}
