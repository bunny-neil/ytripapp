package com.ytripapp.api.client.feign.support;

import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class ApiRequestLocaleResolver extends AcceptHeaderLocaleResolver {

    private String headerName;

    static final Set<Locale> supportedLocales = new HashSet<Locale>(){{
        add(Locale.SIMPLIFIED_CHINESE);
        add(Locale.ENGLISH);
    }};

    public ApiRequestLocaleResolver(String headerName) {
        assert headerName != null;
        this.headerName = headerName;
    }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        return supportedLocales
            .stream()
            .filter(locale -> locale.toString().equals(request.getHeader(headerName)))
            .findFirst()
            .orElse(Locale.ENGLISH);
    }

    public String getHeaderName() {
        return headerName;
    }
}
