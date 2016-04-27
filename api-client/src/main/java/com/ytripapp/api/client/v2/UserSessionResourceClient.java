package com.ytripapp.api.client.v2;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.ytripapp.api.client.v2.command.UserSessionCommand;
import com.ytripapp.api.client.v2.domain.UserSession;
import com.ytripapp.api.client.v2.resource.UserSessionResource;
import com.ytripapp.api.client.feign.decoder.ApiError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public class UserSessionResourceClient {

    @Autowired
    UserSessionResource userSessionResource;

    @HystrixCommand(ignoreExceptions = ApiError.class)
    public UserSession create(UserSessionCommand command) {
        ResponseEntity<UserSession> session = userSessionResource.create(command);
        return session.getBody();
    }

}
