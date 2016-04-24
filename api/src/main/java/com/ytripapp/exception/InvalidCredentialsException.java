package com.ytripapp.exception;

public class InvalidCredentialsException extends ApiException {

    private static final long serialVersionUID = 1091623446460274001L;

    public InvalidCredentialsException() {
        super("invalid.userSession.emailAddressOrPassword");
    }
}
