package com.ytripapp.gateway.security;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationFailureException extends AuthenticationException {

    private static final long serialVersionUID = -3253425408376334320L;

    public AuthenticationFailureException(Throwable t) {
        super("Invalid username or password", t);
    }

}
