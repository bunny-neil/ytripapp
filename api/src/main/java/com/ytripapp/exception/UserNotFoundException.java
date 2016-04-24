package com.ytripapp.exception;

public class UserNotFoundException extends ApiException {

    private static final long serialVersionUID = 6112303785628194789L;

    public UserNotFoundException(Long id) {
        super("notFound.user", id);
    }

}
