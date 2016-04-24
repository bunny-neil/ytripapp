package com.ytripapp.api.support;

import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class HttpHeaderLocaleResolver extends AcceptHeaderLocaleResolver {

    public static final String LOCALE_HEADER_NAME = "X-Ytrip-Locale";

    static final Set<Locale> supportedLocales = new HashSet<Locale>(){{
        add(Locale.SIMPLIFIED_CHINESE);
        add(Locale.ENGLISH);
    }};

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        return supportedLocales
            .stream()
            .filter(locale -> locale.toString().equals(request.getHeader(LOCALE_HEADER_NAME)))
            .findFirst()
            .orElse(Locale.ENGLISH);
    }

}
