package com.ytripapp.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = GatewayConfiguration.class)
public class GatewayApplication {

    public static void main(String...args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
