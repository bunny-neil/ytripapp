package com.ytripapp.api.client.feign.support;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties({"suppressed", "stackTrace", "localizedMessage", "message", "cause"})
public class ApiError extends RuntimeException {

    private static final long serialVersionUID = 3516882047419286172L;

    private int status;
    private String code;
    private String message;
    private List<FieldError> errors = new ArrayList<>();

    @Data
    public static class FieldError {
        String field;
        String message;
    }
}
