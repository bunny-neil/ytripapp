package com.ytripapp.api.controller;

import com.ytripapp.api.support.ApiError;
import com.ytripapp.domain.User;
import com.ytripapp.exception.ApiException;
import com.ytripapp.exception.UserNotFoundException;
import com.ytripapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    MessageSource messageSource;

    @Autowired
    UserService userService;

    @ResponseBody
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handle(ApiException ex, Locale locale) {
        ApiError error = new ApiError();
        error.setCode(ex.getCode());
        error.setMessage(messageSource.getMessage(error.getCode(), ex.getArguments(), locale));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
