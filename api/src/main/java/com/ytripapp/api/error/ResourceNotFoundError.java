package com.ytripapp.api.error;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResourceNotFoundError implements Serializable {

    private static final long serialVersionUID = 8317072150950268613L;

    private String code;
    private String message;

}
