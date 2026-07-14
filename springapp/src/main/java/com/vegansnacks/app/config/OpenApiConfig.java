package com.vegansnacks.app.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger configuration with Bearer Token security scheme
 * so that all secured endpoints can be tested directly from the Swagger UI.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI veganSnacksOpenAPI() {
        final String securitySchemeName = "BearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Vegan Snacks Application System API")
                        .description("Production-ready REST API for the Vegan Snacks Application System. " +
                                "Provides endpoints for authentication, snack management, inventory tracking, " +
                                "and vendor onboarding workflows.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Vegan Snacks Dev Team")
                                .email("dev@vegansnacks.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter your JWT access token")));
    }
}
