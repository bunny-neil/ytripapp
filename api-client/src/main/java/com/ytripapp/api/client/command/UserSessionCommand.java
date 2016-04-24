package com.ytripapp.api.client.command;

import lombok.Data;

@Data
public class UserSessionCommand {

    private String emailAddress;
    private String password;

}
