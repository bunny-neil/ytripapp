package com.ytripapp.api.client.feign.support;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class ApiRequestContextInterceptor implements RequestInterceptor {

    public void apply(RequestTemplate template) {
        ApiRequestContext context = ApiRequestContextHolder.instance().get();
        context.getHeaders().entrySet().stream().forEach(
                entry -> template.header(entry.getKey(), entry.getValue())
        );
    }
}
