package com.ytripapp.api.support;

import com.ytripapp.api.ApiError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;

@ControllerAdvice
public class DefaultControllerAdvice extends ResponseEntityExceptionHandler {

    @Autowired
    protected MessageSource messageSource;

    @ResponseBody
    @ExceptionHandler({Throwable.class})
    public ResponseEntity<ApiError> handleUncaughtException(Throwable t, Locale locale) {
        ApiError error = new ApiError();
        error.setCode("error.internalServer");
        error.setMessage(messageSource.getMessage(error.getCode(), null, locale));
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}