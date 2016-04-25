package com.ytripapp.gateway.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ytripapp.api.client.domain.UserProfile;
import com.ytripapp.api.client.domain.UserSession;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@JsonIgnoreProperties({"username", "password", "accountNonExpired", "accountNonLocked", "credentialsNonExpired"})
public class UserSessionDetails implements UserDetails {

    private Long userId;
    private boolean enabled;
    UserProfile profile;
    private Set<SimpleGrantedAuthority> authorities = new HashSet<>();
    private String username;
    private String password;


    public UserSessionDetails(UserSession session, String username, String password) {
        userId = session.getUserId();
        enabled = session.isEnabled();
        profile = session.getProfile();
        authorities.addAll(
            session.getAuthorities().stream().map(name -> new SimpleGrantedAuthority(name)).collect(Collectors.toSet())
        );
        this.username = username;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
