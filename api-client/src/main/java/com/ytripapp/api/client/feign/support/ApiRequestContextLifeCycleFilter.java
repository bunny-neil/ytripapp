package com.ytripapp.api.client.feign.support;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApiRequestContextLifeCycleFilter extends GenericFilterBean {

    ApiRequestLocaleResolver localeResolver;
    ApiHttpSessionStrategy apiHttpSessionStrategy;

    public ApiRequestContextLifeCycleFilter(
            ApiRequestLocaleResolver localeResolver,
            ApiHttpSessionStrategy apiHttpSessionStrategy) {
        this.localeResolver = localeResolver;
        this.apiHttpSessionStrategy = apiHttpSessionStrategy;
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
        catch (Exception ex) {

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
    }
}
