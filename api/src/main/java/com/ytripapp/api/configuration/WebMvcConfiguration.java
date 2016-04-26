package com.ytripapp.api.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.ytripapp.api.support.HttpHeaderLocaleResolver;
import com.ytripapp.command.validator.UserSessionCommandValidator;
import com.ytripapp.repository.UserRepository;
import com.ytripapp.service.UserService;
import com.ytripapp.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;

public class WebMvcConfiguration extends WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter {

    @Value("${api.locale.header-name:X-Ytrip-Locale}")
    String apiLocaleHaderName;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserSessionCommandValidator sessionCommandValidator() {
        return new UserSessionCommandValidator();
    }

    @Autowired
    @Bean
    UserSessionService userSessionService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new UserSessionService(userRepository, passwordEncoder);
    }

    @Autowired
    @Bean
    UserService userService(UserRepository userRepository) {
        return new UserService(userRepository);
    }

    @Bean
    ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setBasenames("classpath:/messages");
        return messageSource;
    }

    @Bean
    MappingJackson2HttpMessageConverter converter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_ABSENT);
        Hibernate5Module hibernate5Module = new Hibernate5Module();
        hibernate5Module.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
        objectMapper.registerModule(hibernate5Module);
        Jackson2ObjectMapperBuilder.json().configure(objectMapper);
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

    @Bean
    @Override
    public LocaleResolver localeResolver() {
        return new HttpHeaderLocaleResolver(apiLocaleHaderName);
    }
}
