package com.ytripapp.api.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@JsonIgnoreProperties({"password", "username", "accountNonExpired", "accountNonLocked", "credentialsNonExpired"})
public class Passport implements UserDetails {

    UserSession session;
    Collection<? extends GrantedAuthority> authorities;

    public Passport(UserSession session) {
        this.session = session;
        authorities = session.getAuthorities()
            .stream()
            .map(name -> new SimpleGrantedAuthority(name))
            .collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return session.getPassword();
    }

    @Override
    public String getUsername() {
        return session.getEmailAddress();
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
        return session.isEnabled();
    }

    public String getNickname() {
        return session.getNickname();
    }

    public String getPortraitUri() {
        return session.getPortraitUri();
    }
}
