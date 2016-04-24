package com.ytripapp.gateway;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

@EnableDiscoveryClient
@EnableZuulProxy
@EnableAutoConfiguration
@Configuration
public class GatewayConfiguration {

    @Configuration
    static class WebMvcConfiguration extends WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter {

        @Bean
        public HttpSessionStrategy httpSessionStrategy() {
            return new HeaderHttpSessionStrategy();
        }

        @Bean
        MappingJackson2HttpMessageConverter converter() {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_ABSENT);
            Jackson2ObjectMapperBuilder.json().configure(objectMapper);
            return new MappingJackson2HttpMessageConverter(objectMapper);
        }
    }
}
