package com.igar15.training_management;

import com.fasterxml.classmate.TypeResolver;
import com.igar15.training_management.to.swaggerTo.SwaggerUserCreateTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Autowired
    private TypeResolver typeResolver;

    Contact contact = new Contact("Igor Sh", "http://www.igar15.com", "igar15@yandex.ru");

    List<VendorExtension> vendorExtensions = new ArrayList<>();

    ApiInfo apiInfo = new ApiInfo(
            "Training Management App Web Service Documentation",
            "This page documents Training Management RESTful Web Service endpoints",
            "1.0",
            "www.igar15.com/service.html",
            contact,
            "Apache 2.0",
            "http://www.apache.org/licenses/LICENSE-2.0",
            vendorExtensions
    );

    @Bean
    public Docket apiDocket() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.igar15.training_management"))
                .paths(PathSelectors.any())
                .build()
                .additionalModels(typeResolver.resolve(SwaggerUserCreateTo.class));
        return docket;
    }

}
