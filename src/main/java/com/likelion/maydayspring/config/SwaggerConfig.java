package com.likelion.maydayspring.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    servers = @Server(url = "/", description = "Default Server URL"),
    info = @Info(
        title = "MAYDAY 백엔드 API 명세",
        description = "springdoc을 이용한 Swagger API 문서입니다.",
        version = "1.0",
        contact = @Contact(
            name = "springdoc 공식문서",
            url = "https://springdoc.org/"
        )
    )
)
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

}
