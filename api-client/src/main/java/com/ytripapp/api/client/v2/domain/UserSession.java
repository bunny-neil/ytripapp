package com.ytripapp.api.client.v2.domain;

import lombok.Data;

import java.util.Set;

@Data
public class UserSession {

    private Long userId;
    private boolean enabled;
    private String username;
    private String password;
    private Set<String> authorities;
    private UserProfile profile;

}
