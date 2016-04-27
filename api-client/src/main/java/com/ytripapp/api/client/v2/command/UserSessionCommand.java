package com.ytripapp.api.client.v2.command;

import lombok.Getter;

@Getter
public class UserSessionCommand {

    private String emailAddress;
    private String password;

    public UserSessionCommand(String emailAddress, String password) {
        this.emailAddress = emailAddress;
        this.password = password;
    }
}
