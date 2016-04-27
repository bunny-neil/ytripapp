package com.ytripapp.gateway.security;

import com.ytripapp.api.client.v2.UserSessionResourceClient;
import com.ytripapp.api.client.v2.command.UserSessionCommand;
import com.ytripapp.api.client.v2.domain.UserSession;
import com.ytripapp.api.client.feign.decoder.ApiError;
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
        String password = null;
        if (authentication.getCredentials() != null) {
            password = authentication.getCredentials().toString();
        }
        UserSessionCommand command = new UserSessionCommand(ensureNotNull(username), ensureNotNull(password));
        UserSession userSession;
        try {
            userSession = userSessionResourceClient.create(command);
        }
        catch (ApiError error) {
            throw new AuthenticationFailureException(error);
        }
        return new Passport(
            userSession.getUserId(),
            userSession.isEnabled(),
            userSession.getUsername(),
            userSession.getPassword(),
            userSession.getAuthorities()
                .stream().map(name -> new SimpleGrantedAuthority(name)).collect(Collectors.toSet()),
            userSession.getProfile().getNickname(),
            userSession.getProfile().getPortraitUri()
        );
    }

    String ensureNotNull(String s) {
        if (s == null) {
            return "";
        }
        return s;
    }
}
