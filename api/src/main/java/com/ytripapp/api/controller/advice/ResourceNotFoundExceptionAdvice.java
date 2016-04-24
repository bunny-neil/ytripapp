package com.ytripapp.api.controller.advice;

import com.ytripapp.api.error.ResourceNotFoundError;
import com.ytripapp.exception.InvalidCredentialsException;
import com.ytripapp.exception.InvalidEmailAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Locale;

@ControllerAdvice
public class ResourceNotFoundExceptionAdvice {

    @Autowired
    protected MessageSource messageSource;

    @ResponseBody
    @ExceptionHandler({
            InvalidEmailAddressException.class,
            InvalidCredentialsException.class
    })
    public ResponseEntity<ResourceNotFoundError> handle(Locale locale) {
        ResourceNotFoundError error = new ResourceNotFoundError();
        error.setCode("notFound.emailAddressOrPassword");
        error.setMessage(messageSource.getMessage(error.getCode(), null, locale));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
