package com.ytripapp.api.security;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
public class UserSession implements Serializable {

    private static final long serialVersionUID = -6598701510626367768L;

    private Long userId;
    private boolean enabled;
    private String emailAddress;
    private String password;
    private String nickname;
    private String portraitUri;
    private Set<String> authorities = new HashSet<>();
}
