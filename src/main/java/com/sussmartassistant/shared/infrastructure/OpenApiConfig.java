package com.sussmartassistant.shared.infrastructure;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("SUS Smart Assistant API")
                        .description("API do prontuário eletrônico unificado do SUS com assistente " +
                                "inteligente para sugestão de hipóteses diagnósticas via IA (Ollama/LLM). " +
                                "Centraliza dados clínicos de pacientes e oferece apoio à decisão médica.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("SUS Smart Assistant Team")
                                .email("contato@sussmartassistant.com.br")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Insira o token JWT obtido via POST /api/v1/auth/login")));
    }
}
