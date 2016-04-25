package com.ytripapp.api.client.feign.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class ApiRequestContextLifeCycleFilter extends GenericFilterBean {

    ApiRequestLocaleResolver localeResolver;
    ApiHttpSessionStrategy apiHttpSessionStrategy;
    ObjectMapper objectMapper;

    public ApiRequestContextLifeCycleFilter(
            ApiRequestLocaleResolver localeResolver,
            ApiHttpSessionStrategy apiHttpSessionStrategy,
            ObjectMapper objectMapper) {
        this.localeResolver = localeResolver;
        this.apiHttpSessionStrategy = apiHttpSessionStrategy;
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;

        HystrixRequestContext hystrixRequestContext = HystrixRequestContext.initializeContext();
        createApiRequestContext(request);
        try {
            chain.doFilter(request, response);
        }
        catch (HystrixRuntimeException ex) {
            log.error("HystrixRuntimeException", ex.getCause());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.flushBuffer();
        }
        finally {
            hystrixRequestContext.shutdown();
        }
    }

    private void createApiRequestContext(HttpServletRequest request) {
        ApiRequestContext context = new ApiRequestContext();
        context.getHeaders().put(
                localeResolver.getHeaderName(),
                localeResolver.resolveLocale(request).toString());
        context.getHeaders().put(
                apiHttpSessionStrategy.getHeaderName(),
                apiHttpSessionStrategy.getRequestedSessionId(request));
        ApiRequestContextHolder.instance().set(context);
    }
}
