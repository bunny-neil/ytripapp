package com.ytripapp.api.client;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.ytripapp.api.client.command.UserSessionCommand;
import com.ytripapp.api.client.domain.UserSession;
import com.ytripapp.api.client.feign.UserSessionResource;
import com.ytripapp.api.client.feign.support.ApiError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public class UserSessionClient {

    @Autowired
    UserSessionResource userSessionResource;

    @HystrixCommand(ignoreExceptions = ApiError.class)
    public UserSession create(UserSessionCommand command) {
        ResponseEntity<UserSession> session = userSessionResource.create(command);
        return session.getBody();
    }

}
