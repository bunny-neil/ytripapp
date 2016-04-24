package com.ytripapp.command;

import lombok.Data;

@Data
public class UserSessionCommand {

    private String emailAddress;
    private String password;

}
