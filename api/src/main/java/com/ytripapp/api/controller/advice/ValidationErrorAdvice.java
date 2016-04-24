package com.ytripapp.api.controller.advice;

import com.ytripapp.api.error.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Locale;

@ControllerAdvice
public class ValidationErrorAdvice /*extends DefaultHandlerExceptionResolver*/ {

    @Autowired
    MessageSource messageSource;

    @ResponseBody
    @ExceptionHandler({
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ValidationError> handle(Exception ex, Locale locale) {
        ValidationError error = null;
        if (MethodArgumentNotValidException.class.isAssignableFrom(ex.getClass())) {
            error =handle((MethodArgumentNotValidException)ex, locale);
        }
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    ValidationError handle(MethodArgumentNotValidException ex, Locale locale) {
        ValidationError error = new ValidationError();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                        error.getErrors().put(
                                fieldError.getField(),
                                messageSource.getMessage(fieldError.getCode(), null, locale))
        );
        return error;
    }
}
