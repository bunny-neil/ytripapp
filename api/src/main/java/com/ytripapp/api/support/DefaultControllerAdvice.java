package com.ytripapp.api.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;

@ControllerAdvice
public class DefaultControllerAdvice {

    @Autowired
    protected MessageSource messageSource;

    /*@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @ExceptionHandler({Throwable.class})
    public ApiError handleUncaughtException(Throwable t, Locale locale) {
        ApiError error = new ApiError();
        error.setCode("error.internalServer");
        error.setMessage(messageSource.getMessage(error.getCode(), null, locale));
        return error;
    }*/

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ApiError handleValidationException(MethodArgumentNotValidException ex, Locale locale) {
        ApiError error = new ApiError();
        error.setCode("error.validation");
        error.setMessage(messageSource.getMessage(error.getCode(), null, locale));
        ex.getBindingResult().getFieldErrors().stream()
            .forEach(fieldError ->
                    error.getErrors().put(
                        fieldError.getField(),
                        messageSource.getMessage(fieldError.getCode(), fieldError.getArguments(), locale))
            );
        return error;
    }
}