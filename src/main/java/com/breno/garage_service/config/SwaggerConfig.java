package com.breno.garage_service.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI garageServiceAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Garage Service API - Estapar Challenge by Breno Delgado")
                        .description("API para gerenciamento de estacionamento (Desafio Estapar)")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Breno")
                                .email("brenodelgado.dev@gmail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentação técnica do desafio Estapar")
                        .url("https://github.com/brenodevsys/desafio-estapar"));
    }
}
