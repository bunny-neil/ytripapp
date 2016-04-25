package com.ytripapp.gateway;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ytripapp.api.client.ClientConfiguration;
import com.ytripapp.api.client.UserSessionResourceClient;
import com.ytripapp.gateway.security.ApiAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

@EnableDiscoveryClient
@EnableZuulProxy
@EnableAutoConfiguration
@Import(ClientConfiguration.class)
@Configuration
public class GatewayConfiguration {

    @Configuration
    static class WebMvcConfiguration extends WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter {
        @Bean
        MappingJackson2HttpMessageConverter converter() {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_ABSENT);
            Jackson2ObjectMapperBuilder.json().configure(objectMapper);
            return new MappingJackson2HttpMessageConverter(objectMapper);
        }
    }

    @Configuration
    static class SecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        UserSessionResourceClient userSessionResourceClient;

        @Autowired
        MappingJackson2HttpMessageConverter converter;

        @Bean
        public HttpSessionStrategy httpSessionStrategy() {
            return new HeaderHttpSessionStrategy();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(new ApiAuthenticationProvider(userSessionResourceClient));
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                    .authorizeRequests()
                    .anyRequest().fullyAuthenticated()
                .and()
                    .formLogin()
                        .loginProcessingUrl("/api/v2/sessions");
        }
    }
}
