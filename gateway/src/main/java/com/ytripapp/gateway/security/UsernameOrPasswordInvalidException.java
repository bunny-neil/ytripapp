package com.ytripapp.gateway.security;

import org.springframework.security.core.AuthenticationException;

public class UsernameOrPasswordInvalidException extends AuthenticationException {

    public UsernameOrPasswordInvalidException() {
        super("Invalid username or password.");
    }

}
