package com.ytripapp.gateway.security;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    MappingJackson2HttpMessageConverter converter;

    public AuthenticationSuccessHandler(MappingJackson2HttpMessageConverter converter) {
        this.converter = converter;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        super.clearAuthenticationAttributes(request);
        UserSessionDetails details = (UserSessionDetails)authentication.getPrincipal();
        converter.write(details, MediaType.APPLICATION_JSON_UTF8, new ServletServerHttpResponse(response));
    }
}
