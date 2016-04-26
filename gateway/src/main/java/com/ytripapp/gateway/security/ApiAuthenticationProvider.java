package com.ytripapp.gateway.security;

import com.ytripapp.api.client.UserSessionResourceClient;
import com.ytripapp.api.client.command.UserSessionCommand;
import com.ytripapp.api.client.domain.UserSession;
import com.ytripapp.api.security.Passport;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.stream.Collectors;

public class ApiAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    UserSessionResourceClient userSessionResourceClient;

    public ApiAuthenticationProvider(UserSessionResourceClient userSessionResourceClient) {
        this.userSessionResourceClient = userSessionResourceClient;
    }

    @Override
    protected void additionalAuthenticationChecks(
        UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
        throws AuthenticationException {
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
        throws AuthenticationException {
        String password = authentication.getCredentials().toString();
        UserSessionCommand command = new UserSessionCommand();
        command.setEmailAddress(username);
        command.setPassword(password);
        UserSession userSession = userSessionResourceClient.create(command);
        return new Passport(
            userSession.getUserId(),
            userSession.isEnabled(),
            userSession.getUsername(),
            userSession.getPassword(),
            userSession.getAuthorities()
                .stream().map(name -> new SimpleGrantedAuthority(name)).collect(Collectors.toSet()),
            userSession.getProfile()
        );
    }
}
