package com.ytripapp.api.support;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import java.util.Locale;
import java.util.stream.Collectors;

@ControllerAdvice
public class DefaultControllerAdvice {

    @Autowired
    protected MessageSource messageSource;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiError handleValidationException(MethodArgumentNotValidException ex, Locale locale) {
        ApiError error = createBadRequestError(locale);
        error.getErrors().addAll(
            ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError ->
                    new ApiError.FieldError(
                        fieldError.getField(),
                        messageSource.getMessage(fieldError.getCode(), fieldError.getArguments(), locale)))
                .collect(Collectors.toList())
        );
        return error;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler({
        HttpMessageNotReadableException.class,
        HttpRequestMethodNotSupportedException.class,
        NoSuchRequestHandlingMethodException.class,
        HttpMediaTypeNotSupportedException.class,
        HttpMediaTypeNotAcceptableException.class,
        MissingPathVariableException.class,
        MissingServletRequestParameterException.class,
        ServletRequestBindingException.class,
        ConversionNotSupportedException.class,
        TypeMismatchException.class,
        HttpMessageNotWritableException.class,
        MissingServletRequestPartException.class,
        BindException.class,
        NoHandlerFoundException.class
    })
    public ApiError handle(HttpMessageNotReadableException ex, Locale locale) {
        return createBadRequestError(locale);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @ExceptionHandler({Throwable.class})
    public ApiError handleUncaughtException(Throwable t, Locale locale) {
        ApiError error = new ApiError();
        error.setCode("error.internalServer");
        error.setMessage(messageSource.getMessage(error.getCode(), null, locale));
        return error;
    }

    ApiError createBadRequestError(Locale locale) {
        ApiError error = new ApiError();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setCode("error.validation");
        error.setMessage(messageSource.getMessage(error.getCode(), null, locale));
        return error;
    }
}