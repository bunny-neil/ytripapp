package com.ytripapp.api.support;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
public class ApiError {

    private int status;
    private String code;
    private String message;
    private List<FieldError> errors = new ArrayList<>();

    @Getter
    public static class FieldError {
        String field;
        String message;

        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }
    }
}
