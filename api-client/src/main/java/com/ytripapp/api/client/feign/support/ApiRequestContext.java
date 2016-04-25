package com.ytripapp.api.client.feign.support;

import lombok.Data;

import java.util.Locale;

@Data
public class ApiRequestContext {

    /*private String username;
    private String password;*/
    private Locale locale;

}
