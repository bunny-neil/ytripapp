package com.ytripapp.api.configuration;

import com.ytripapp.api.support.HttpHeaderLocaleResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

@Configuration
public class WebMvcConfiguration extends WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter {

    @Value("${api.locale.header-name:X-Ytrip-Locale}")
    String apiLocaleHaderName;

    @Bean
    @Override
    public LocaleResolver localeResolver() {
        return new HttpHeaderLocaleResolver(apiLocaleHaderName);
    }
}
