package com.ytripapp.api.controller;

import com.ytripapp.api.ApiError;
import com.ytripapp.command.UserSessionCommand;
import com.ytripapp.command.validator.UserSessionCommandValidator;
import com.ytripapp.domain.UserSession;
import com.ytripapp.exception.InvalidCredentialsException;
import com.ytripapp.exception.InvalidEmailAddressException;
import com.ytripapp.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.DataBinder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    @Autowired
    MessageSource messageSource;

    @Autowired
    UserSessionCommandValidator commandValidator;

    @Autowired
    UserSessionService userSessionService;

    @InitBinder
    public void initBinder(DataBinder binder) {
        binder.addValidators(commandValidator);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserSession> create(@RequestBody @Validated UserSessionCommand command) {
        return new ResponseEntity<>(userSessionService.create(command), HttpStatus.OK);
    }

    @ResponseBody
    @ExceptionHandler({
        InvalidEmailAddressException.class,
        InvalidCredentialsException.class
    })
    public ResponseEntity<ApiError> handleSessionCreationException(Locale locale) {
        ApiError error = new ApiError();
        error.setCode("invalid.userSession.emailAddressOrPassword");
        error.setMessage(messageSource.getMessage(error.getCode(), null, locale));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
