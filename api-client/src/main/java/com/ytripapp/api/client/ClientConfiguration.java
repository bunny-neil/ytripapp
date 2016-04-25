package com.ytripapp.api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.ytripapp.api.client.feign.UserSessionResource;
import com.ytripapp.api.client.feign.decoder.CustomObjectDecoder;
import com.ytripapp.api.client.feign.decoder.FeignErrorDecoder;
import com.ytripapp.api.client.feign.decoder.FeignResponseDecoder;
import com.ytripapp.api.client.feign.decoder.PageDecoder;
import com.ytripapp.api.client.feign.support.ApiRequestContextInterceptor;
import com.ytripapp.api.client.feign.support.ApiRequestContextLifeCycleFilter;
import com.ytripapp.api.client.feign.support.ApiRequestLocaleResolver;
import com.ytripapp.api.client.feign.support.HystrixConcurrencyStrategy;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.support.ResponseEntityDecoder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
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

        @Value("${api.locale.header-name:X-Ytrip-Locale}")
        String apiLocaleHaderName;

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
        ApiRequestLocaleResolver localeResolver() {
            return new ApiRequestLocaleResolver(apiLocaleHaderName);
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            HystrixPlugins.getInstance().registerConcurrencyStrategy(new HystrixConcurrencyStrategy());
        }

        @Bean
        public HeaderHttpSessionStrategy headerHttpSessionStrategy() {
            return new HeaderHttpSessionStrategy();
        }

        @Autowired
        @Bean
        FilterRegistrationBean apiRequestContextFilter(
                ApiRequestLocaleResolver localeResolver, HeaderHttpSessionStrategy headerHttpSessionStrategy) {
            FilterRegistrationBean bean = new FilterRegistrationBean();
            bean.setFilter(new ApiRequestContextLifeCycleFilter(localeResolver, headerHttpSessionStrategy));
            bean.setOrder(SecurityProperties.DEFAULT_FILTER_ORDER - 1);
            return bean;
        }

        @Bean
        ApiRequestContextInterceptor apiRequestContextInterceptor() {
            return new ApiRequestContextInterceptor();
        }
    }
}
