package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@SpringBootApplication
public class Application {

    public static void main(String[] args){
        /*
            Start application. Schema accessible at:
            http://localhost:8080/swagger-ui/index.html
            http://localhost:8080/v3/api-docs
         */
        SpringApplication.run(Application.class);
    }


    @Bean
    public Docket swaggerApi() {
        /*
            Needed to activate and configure the OpenAPI schema
         */
        return new Docket(DocumentationType.OAS_30)
                .select()
                .paths(PathSelectors.ant("/api/**"))
                .build();
    }
}
