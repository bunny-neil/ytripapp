package com.ytripapp.api.client.feign.support;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonIgnoreProperties({"suppressed", "stackTrace", "localizedMessage", "message", "cause"})
public class ApiError extends RuntimeException {

    private static final long serialVersionUID = 3516882047419286172L;

    private String code;
    private String message;
    private Map<String, String> errors = new HashMap<>();

}
