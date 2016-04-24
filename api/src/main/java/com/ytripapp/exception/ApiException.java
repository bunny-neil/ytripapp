package com.ytripapp.exception;

import lombok.Getter;

@Getter
public abstract class ApiException extends RuntimeException {

    private static final long serialVersionUID = 7607274136243254836L;

    private String code;
    private Object[] arguments;

    public ApiException(String code, Object...arguments) {
        this.code = code;
        this.arguments = arguments;
    }

}
