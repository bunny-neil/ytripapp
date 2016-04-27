package com.ytripapp.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class UserSession {

    private Long userId;
    private boolean enabled;
    private String username;
    private String password;
    private Set<String> authorities;
    private UserProfile profile;
}
