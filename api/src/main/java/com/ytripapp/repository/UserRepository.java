package com.ytripapp.repository;

import com.ytripapp.domain.User;

public interface UserRepository extends SearchableRepository<User, Long> {

    User findByEmailAddress(String emailAddress);

}
