package com.zerobase.fintech.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(
        title = "Fintech API Document",
        description = "API Document",
        version = "v0.1",
        contact = @Contact(
            name = "Kwon",
            email = "kwonfelix93@gmail.com"
        )
    ),
    tags = {
        @Tag(name = "1. USER", description = "회원 기능")
    }
)
@Configuration
public class SwaggerConfig {

}
