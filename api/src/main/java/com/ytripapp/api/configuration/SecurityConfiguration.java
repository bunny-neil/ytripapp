package com.ytripapp.api.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

@Profile("security")
@EnableWebSecurity
@Import({
    SecurityAutoConfiguration.class,
    ManagementWebSecurityAutoConfiguration.class
})
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${api.session.header-name:X-Ytrip-Session}")
    String apiSessionHeaderName;

    @Bean
    public HttpSessionStrategy httpSessionStrategy() {
        HeaderHttpSessionStrategy sessionStrategy = new HeaderHttpSessionStrategy();
        sessionStrategy.setHeaderName(apiSessionHeaderName);
        return sessionStrategy;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
            .and()
            .authorizeRequests()
            .antMatchers("/sessions/**", "/health").permitAll()
            .anyRequest().authenticated();
    }
}
