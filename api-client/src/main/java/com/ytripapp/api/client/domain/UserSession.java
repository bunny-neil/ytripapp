package com.ytripapp.api.client.domain;

import lombok.Data;

import java.util.Set;

@Data
public class UserSession {

    private Long userId;
    private boolean enabled;
    private UserProfile profile;
    private Set<String> authorities;

}
