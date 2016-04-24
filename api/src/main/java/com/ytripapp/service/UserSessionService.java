package com.ytripapp.service;

import com.ytripapp.command.UserSessionCommand;
import com.ytripapp.domain.User;
import com.ytripapp.domain.UserSession;
import com.ytripapp.exception.InvalidCredentialsException;
import com.ytripapp.exception.UserNotFoundException;
import com.ytripapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class UserSessionService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserSessionService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserSession create(UserSessionCommand command) {
        Optional<User> found = Optional.ofNullable(userRepository.findByEmailAddress(command.getEmailAddress()));
        if (! found.isPresent()) {
            throw new UserNotFoundException();
        }
        User user = found.get();
        if (! passwordEncoder.matches(command.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }
        return UserSession.builder().enabled(user.isEnabled()).profile(user.getProfile()).userId(user.getId()).build();
    }

}
