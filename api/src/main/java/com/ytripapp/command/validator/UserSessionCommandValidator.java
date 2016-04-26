package com.ytripapp.command.validator;

import com.ytripapp.command.UserSessionCommand;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UserSessionCommandValidator implements Validator {

    EmailValidator emailValidator = EmailValidator.getInstance();

    @Override
    public boolean supports(Class<?> clazz) {
        return UserSessionCommand.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserSessionCommand command = (UserSessionCommand)target;
        if (! emailValidator.isValid(command.getEmailAddress())) {
            errors.rejectValue("emailAddress", "invalid.userSession.emailAddressOrPassword");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "empty.userSession.password");
    }
}
