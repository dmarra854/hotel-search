package com.hotel.search.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI hotelSearchOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hotel Search API")
                        .description("REST API for hotel availability searches with Kafka-backed persistence")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Hotel Search Team")
                                .email("team@hotelsearch.com")));
    }
}
