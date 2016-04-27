package com.ytripapp.api.client.feign;

import com.ytripapp.api.client.command.UserSessionCommand;
import com.ytripapp.api.client.domain.UserSession;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.ytripapp.api.client.ClientConfiguration.API_VERSION;

@FeignClient("api-" + API_VERSION)
@RequestMapping("/sessions")
public interface UserSessionResource {

    @ResponseBody
    @RequestMapping(
        method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserSession> create(UserSessionCommand command);
}
