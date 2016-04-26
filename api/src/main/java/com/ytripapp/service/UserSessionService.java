package com.ytripapp.service;

import com.ytripapp.command.UserSessionCommand;
import com.ytripapp.domain.Authority;
import com.ytripapp.domain.User;
import com.ytripapp.domain.UserSession;
import com.ytripapp.exception.InvalidCredentialsException;
import com.ytripapp.exception.InvalidEmailAddressException;
import com.ytripapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.Collectors;

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
            throw new InvalidEmailAddressException();
        }
        User user = found.get();
        if (! passwordEncoder.matches(command.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }
        return UserSession
            .builder()
            .enabled(user.isEnabled())
            .profile(user.getProfile())
            .userId(user.getId())
            .username(user.getEmailAddress())
            .password(user.getPassword())
            .authorities(user.getAuthorities().stream().map(Authority::name).collect(Collectors.toSet()))
            .build();
    }

}
