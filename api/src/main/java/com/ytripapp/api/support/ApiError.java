package com.ytripapp.api.support;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ApiError {

    private String code;
    private String message;
    private Map<String, String> errors = new HashMap<>();

}
