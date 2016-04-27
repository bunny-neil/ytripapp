package com.ytripapp.api.client.feign.support;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties({"suppressed", "stackTrace", "localizedMessage", "cause"})
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
