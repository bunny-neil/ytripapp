package com.ytripapp.gateway.security;

import com.ytripapp.api.client.feign.decoder.ApiError;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFailureHandler implements org.springframework.security.web.authentication.AuthenticationFailureHandler {

    MappingJackson2HttpMessageConverter converter;

    public AuthenticationFailureHandler(MappingJackson2HttpMessageConverter converter) {
        this.converter = converter;
    }

    @Override
    public void onAuthenticationFailure(
        HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
        throws IOException, ServletException {
        Throwable t = exception.getCause();
        if (ApiError.class.isAssignableFrom(t.getClass())) {
            ApiError error = (ApiError)t;
            response.setStatus(error.getStatus());
            converter.write(error, MediaType.APPLICATION_JSON_UTF8, new ServletServerHttpResponse(response));
        }
    }
}
