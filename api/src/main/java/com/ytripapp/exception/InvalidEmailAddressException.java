package com.ytripapp.exception;

public class InvalidEmailAddressException extends ApiException {

    private static final long serialVersionUID = 6112303785628194789L;

    public InvalidEmailAddressException() {
        super("notFound.userSession");
    }
}
