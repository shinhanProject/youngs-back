package com.youngs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfig {
    private static final String SECURITY_SCHEMA_NAME = "Authorization";
    public static final String SECURITY_TOKEN_HEADER = "Authorization";
    public static final String AUTHORIZATION_SCOPE_GLOBAL = "global";
    public static final String AUTHORIZATION_SCOPE_GLOBAL_DESC = "accessEverything";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .securityContexts(List.of(securityContext()))
                .securitySchemes(List.of(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.youngs"))
                .paths(PathSelectors.any())
                .build()
                .ignoredParameterTypes(AuthenticationPrincipal.class); // @AuthenticationPrincipal의 파라미터 요구 필드를 없앤다.
    }

    private ApiKey apiKey() {
        return new ApiKey(SECURITY_SCHEMA_NAME, SECURITY_TOKEN_HEADER, "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope(AUTHORIZATION_SCOPE_GLOBAL, AUTHORIZATION_SCOPE_GLOBAL_DESC);
        List<AuthorizationScope> authorizationScopes = new ArrayList<>();
        authorizationScopes.add(authorizationScope);

        return List.of(new SecurityReference(SECURITY_SCHEMA_NAME, authorizationScopes.toArray(new AuthorizationScope[0])));
    }
}