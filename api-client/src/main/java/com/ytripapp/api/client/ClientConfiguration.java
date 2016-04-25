package com.ytripapp.api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.ytripapp.api.client.feign.UserSessionResource;
import com.ytripapp.api.client.feign.decoder.CustomObjectDecoder;
import com.ytripapp.api.client.feign.decoder.FeignErrorDecoder;
import com.ytripapp.api.client.feign.decoder.FeignResponseDecoder;
import com.ytripapp.api.client.feign.decoder.PageDecoder;
import com.ytripapp.api.client.feign.support.ApiRequestContextInterceptor;
import com.ytripapp.api.client.feign.support.ApiRequestLocaleResolver;
import com.ytripapp.api.client.feign.support.HystrixConcurrencyStrategy;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.support.ResponseEntityDecoder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

import java.util.ArrayList;

@EnableHystrix
@EnableCircuitBreaker
@EnableFeignClients(basePackageClasses = UserSessionResource.class)
@Configuration
public class ClientConfiguration {

    // the version must be the same as branch name
    public static final String API_VERSION = "v2";

    @Bean
    UserSessionResourceClient userSessionClient() {
        return new UserSessionResourceClient();
    }

    @Configuration
    static class FeignSupportConfiguration implements InitializingBean {

        @Autowired
        ObjectMapper objectMapper;

        @Autowired
        ObjectFactory<HttpMessageConverters> messageConverters;

        @Bean
        ErrorDecoder feignErrorDecoder() {
            return new FeignErrorDecoder(objectMapper);
        }

        @Bean
        ResponseEntityDecoder responseEntityDecoder() {
            return new ResponseEntityDecoder(
                    new FeignResponseDecoder(new ArrayList<CustomObjectDecoder>(){{
                        add(new PageDecoder(objectMapper));
                    }}, messageConverters));
        }

        @Bean
        LocaleResolver localeResolver() {
            return new ApiRequestLocaleResolver();
        }

        @Bean
        ApiRequestContextInterceptor localeFeignInterceptor() {
            return new ApiRequestContextInterceptor();
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            HystrixPlugins.getInstance().registerConcurrencyStrategy(new HystrixConcurrencyStrategy());
        }
    }
}
