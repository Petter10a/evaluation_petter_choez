package com.kevaluacion.evaluacion_petter_choez.Config.Swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

/**
 * Swagger configuration class for setting up OpenAPI documentation.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Prueba kevaluacion")
                        .version("1.0")
                        .description("Documentación interactiva de prueba kevaluacion")
                        .contact(new Contact()
                                .name("Petter Choez")
                                .email("petter.choez@sasf.net")));
    }
}
