package com.ytripapp.api.client.feign.support;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ApiRequestContext {

    Map<String, String> headers = new HashMap<>();

}
