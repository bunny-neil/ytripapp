package com.ytripapp.api.error;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class ValidationError implements Serializable {

    private static final long serialVersionUID = 2854651842251410003L;

    private Map<String, String> errors = new HashMap<>();

}
