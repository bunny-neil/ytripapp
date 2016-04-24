package com.ytripapp.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSession {

    private Long userId;
    private boolean enabled;
    private UserProfile profile;

}
