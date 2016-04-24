package com.ytripapp.service;

import com.ytripapp.domain.User;
import com.ytripapp.exception.UserNotFoundException;
import com.ytripapp.repository.UserRepository;

import java.util.Optional;

public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(Long id) {
        Optional<User> found = Optional.ofNullable(userRepository.findOne(id));
        if (! found.isPresent()) {
            throw new UserNotFoundException(id);
        }
        return found.get();
    }
}
