package com.breno.garage_service.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AppConfig {
    @Bean
    RestClient restClient(RestClient.Builder builder) {
        return builder.build();
    }
}