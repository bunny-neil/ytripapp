package com.ytripapp.gateway.support;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.ytripapp.api.client.feign.support.ApiRequestContext;
import com.ytripapp.api.client.feign.support.ApiRequestContextHolder;
import com.ytripapp.api.client.feign.support.ApiRequestLocaleResolver;
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

    LocaleResolver localeResolver = new ApiRequestLocaleResolver();

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
        finally {
            hystrixRequestContext.shutdown();
        }
    }

    private void createApiRequestContext(HttpServletRequest request) {
        ApiRequestContext context = new ApiRequestContext();
        context.setLocale(localeResolver.resolveLocale(request));
        ApiRequestContextHolder.instance().set(context);
        /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            UserDetailsAdapter adapter = (UserDetailsAdapter)authentication.getPrincipal();
            if (adapter.getLogin() != null) {
                context.setUsername(adapter.getLogin().getUsername());
                context.setPassword(adapter.getLogin().getPassword());
            }
        }*/
    }
}
