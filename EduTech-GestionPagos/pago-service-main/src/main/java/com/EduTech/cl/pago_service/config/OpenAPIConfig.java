package com.EduTech.cl.pago_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API de Pagos - EduTech")
                .version("1.0")
                .description("Microservicio encargado de gestionar el carro de compras y los pagos de los cursos.")
                .contact(new Contact()
                    .name("Equipo EduTech")
                    .email("soporte@edutech.com")));
    }

}
