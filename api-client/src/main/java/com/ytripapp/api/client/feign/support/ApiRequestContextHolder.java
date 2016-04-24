package com.ytripapp.api.client.feign.support;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;

public class ApiRequestContextHolder {

    private static final HystrixRequestVariableDefault<ApiRequestContext> holder =
        new HystrixRequestVariableDefault<>();

    public static HystrixRequestVariableDefault<ApiRequestContext> instance() {
        return holder;
    }

    private ApiRequestContextHolder(){}

}
