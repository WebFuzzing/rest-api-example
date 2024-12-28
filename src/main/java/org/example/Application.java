package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args){
        /*
            Start application. Schema accessible at:
            http://localhost:8080/v3/api-docs
         */
        SpringApplication.run(Application.class);
    }
}
