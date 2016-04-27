package com.ytripapp.api.client.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.ytripapp.api.client.feign.decoder.CustomObjectDecoder;
import com.ytripapp.api.client.feign.decoder.FeignResponseDecoder;
import com.ytripapp.api.client.feign.support.ApiHttpSessionStrategy;
import com.ytripapp.api.client.feign.support.ApiRequestContextInterceptor;
import com.ytripapp.api.client.feign.support.ApiRequestContextLifeCycleFilter;
import com.ytripapp.api.client.feign.support.ApiRequestLocaleResolver;
import com.ytripapp.api.client.feign.support.HystrixConcurrencyStrategy;
import com.ytripapp.api.client.v2.resource.UserSessionResource;
import com.ytripapp.api.client.feign.decoder.ApiErrorDecoder;
import com.ytripapp.api.client.feign.decoder.PageDecoder;
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

import java.util.ArrayList;

@EnableHystrix
@EnableCircuitBreaker
@EnableFeignClients(basePackageClasses = UserSessionResource.class)
@Configuration
public class V2ClientConfiguration {

    public static final String API_VERSION = "v2";

    @Bean
    UserSessionResourceClient userSessionClient() {
        return new UserSessionResourceClient();
    }

    @Configuration
    static class FeignSupportConfiguration implements InitializingBean {

        @Value("${api.locale.header-name:X-Ytrip-Locale}")
        String apiLocaleHaderName;

        @Value("${api.session.header-name:X-Ytrip-Session}")
        String apiSessionHeaderName;

        @Autowired
        ObjectMapper objectMapper;

        @Autowired
        ObjectFactory<HttpMessageConverters> messageConverters;

        @Bean
        ErrorDecoder feignErrorDecoder() {
            return new ApiErrorDecoder(objectMapper);
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
        public ApiHttpSessionStrategy headerHttpSessionStrategy() {
            return new ApiHttpSessionStrategy(apiSessionHeaderName);
        }

        @Autowired
        @Bean
        FilterRegistrationBean apiRequestContextFilter(
                ApiRequestLocaleResolver localeResolver, ApiHttpSessionStrategy apiHttpSessionStrategy) {
            FilterRegistrationBean bean = new FilterRegistrationBean();
            bean.setFilter(new ApiRequestContextLifeCycleFilter(localeResolver, apiHttpSessionStrategy, objectMapper));
            bean.setOrder(SecurityProperties.DEFAULT_FILTER_ORDER - 1);
            return bean;
        }

        @Bean
        ApiRequestContextInterceptor apiRequestContextInterceptor() {
            return new ApiRequestContextInterceptor();
        }
    }
}
