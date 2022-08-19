package co.unthinkable.userservice.configurations;

import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;

import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Configuration
@ConditionalOnProperty(value = "springfox.documentation.enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerConfig {

@Bean
public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .securityContexts(Arrays.asList(securityContext()))
            .securitySchemes(Arrays.asList(apiKey()))
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build();
}

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Documentation for User Service ")
                .description("user service")
                .contact(new Contact("Hritik Khatri", "unthinkable.co", "hritik.khatri@unthinkable.co"))
                .version("1.0.0")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }
//}
//    @Bean
//    public Docket actuatorApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("Spring Actuator")
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(apiInfo())
//                .securityContexts(Arrays.asList(actuatorSecurityContext()))
//                .securitySchemes(Arrays.asList(basicAuthScheme()));
//    }
//
//    private SecurityContext actuatorSecurityContext() {
//        return SecurityContext.builder()
//                .securityReferences(Arrays.asList(basicAuthReference()))
//                .forPaths(PathSelectors.any())
//                .build();
//    }
//
//    private SecurityScheme basicAuthScheme() {
//        return new BasicAuth("basicAuth");
//    }
//
//    private SecurityReference basicAuthReference() {
//        return new SecurityReference("basicAuth", new AuthorizationScope[0]);
//    }
//        private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("Swagger3")
//                .description("user service")
//                .contact(new Contact("name", "url", "contact"))
//                .version("1.0.0")
//                .build();
//    }
}