package com.ytripapp.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
public class UserSession {

    private Long userId;
    private boolean enabled;
    private UserProfile profile;
    private Set<String> authorities;
}
