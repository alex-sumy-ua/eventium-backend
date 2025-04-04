package com.gamboom.eventium.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig { // Responsible for configuring OpenAPI (Swagger) documentation for the API

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Eventium API")
                        .version("1.0")
                        .description("API for managing users, events, and registrations"));
    }

}
