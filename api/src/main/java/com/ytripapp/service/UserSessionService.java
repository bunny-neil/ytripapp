package com.ytripapp.service;

import com.ytripapp.api.security.UserSession;
import com.ytripapp.command.UserSessionCommand;
import com.ytripapp.domain.Authority;
import com.ytripapp.domain.User;
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
        UserSession session = new UserSession();
        session.getAuthorities().addAll(
            user.getAuthorities()
                .stream()
                .map(Authority::name)
                .collect(Collectors.toSet())
        );
        session.setEmailAddress(user.getEmailAddress());
        session.setEnabled(user.isEnabled());
        session.setNickname(user.getProfile().getNickname());
        session.setPassword(user.getPassword());
        session.setPortraitUri(user.getProfile().getPortraitUri());
        session.setUserId(user.getId());
        return session;
    }
}
