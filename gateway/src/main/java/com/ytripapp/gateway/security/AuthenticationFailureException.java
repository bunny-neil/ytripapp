package com.ytripapp.gateway.security;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationFailureException extends AuthenticationException {

    public AuthenticationFailureException(Throwable t) {
        super("Invalid username or password", t);
    }

}
