package es.kiwi.common.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket buildDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(buildApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("es.kiwi")) // 要扫描的API(Controller)基础包
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 描述性文档
     * @return
     */
    private ApiInfo buildApiInfo() {
        Contact contact = new Contact("Kiwi Developer", "", "");
        return new ApiInfoBuilder()
                .title("Kiwi Headlines - Platform Management API Documentation")
                .description("Kiwi Headlines - BackOffice API")
                .contact(contact)
                .version("1.0.0").build();
    }
}
