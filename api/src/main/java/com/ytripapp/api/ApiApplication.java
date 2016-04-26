package com.ytripapp.api;

import com.ytripapp.api.configuration.DefaultConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = DefaultConfiguration.class)
public class ApiApplication {

    public static void main(String...args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
