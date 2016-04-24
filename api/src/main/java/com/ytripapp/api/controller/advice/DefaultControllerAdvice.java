package com.ytripapp.api.controller.advice;

import com.ytripapp.api.error.InternalServerError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Locale;

@ControllerAdvice
public class DefaultControllerAdvice {

    @Autowired
    protected MessageSource messageSource;

    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<InternalServerError> handle(Throwable ex, Locale locale) {
        return new ResponseEntity<>(
                new InternalServerError(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}
