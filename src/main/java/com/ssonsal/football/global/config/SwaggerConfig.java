package com.ssonsal.football.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "ssonsal",
                description = "동네 공놀이 매칭 프로젝트",
                version = "v1"
        )
)
@Configuration
public class SwaggerConfig {

}
