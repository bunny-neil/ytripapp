package com.ytripapp.api.client.feign.support;

import lombok.Data;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Data
public class ApiRequestContext {

    /*private String username;
    private String password;*/
    Map<String, String> headers = new HashMap<>();

}
