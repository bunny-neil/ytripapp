package com.ytripapp.api.client.feign.support;

import org.springframework.session.web.http.HeaderHttpSessionStrategy;

public class ApiHttpSessionStrategy extends HeaderHttpSessionStrategy {

    private String headerName;

    public ApiHttpSessionStrategy(String headerName) {
        assert headerName != null;
        this.headerName = headerName;
        setHeaderName(headerName);
    }

    public String getHeaderName() {
        return headerName;
    }
}
