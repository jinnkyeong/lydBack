package com.loveyourdog.brokingservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {
    private String version = "V0.1";


    // JWT + swagger
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }


    // Swagger 상단에 표시될 제목, 설명, 버전, 작성자 정보 등을 표시
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder() // API에 대한 정보를 설정
                .title("제목")
                .description("설명")
                .version(version)
                .contact(new Contact("이름", "홈페이지 URL", "e-mail"))
                .build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                // select() : ApiSelectorBuilder 클래스의 인스턴스를 반환
                // (해당 인스턴스를 통하여 Swagger의 end-point를 제어 가능)
                .select()
                // 전체 API에 대한 문서를 Swagger를 통해 나타낼 수 있음
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo()); // Swagger 상단에 표시될 제목, 설명, 버전, 작성자 정보 등을 표시
    }
}
