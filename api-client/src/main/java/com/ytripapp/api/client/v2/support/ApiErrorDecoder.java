package com.ytripapp.api.client.v2.support;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import feign.FeignException;
import feign.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Iterator;

@Slf4j
public class ApiErrorDecoder implements feign.codec.ErrorDecoder {

    ObjectMapper objectMapper;

    public ApiErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            switch (response.status()) {
                case 400:
                case 404:
                case 500:
                    ApiError error = new ApiError();
                    error.setStatus(response.status());

                    JsonNode rootNode = objectMapper.readTree(response.body().asInputStream());
                    JsonNode codeNode = rootNode.get("code");
                    if (codeNode != null) {
                        error.setCode(codeNode.asText());
                    }
                    JsonNode messageNode = rootNode.get("message");
                    if (messageNode != null) {
                        error.setMessage(messageNode.asText());
                    }

                    Iterator<JsonNode> errorsIterator = rootNode.get("errors").elements();
                    while (errorsIterator.hasNext()) {
                        ApiError.FieldError fieldError = objectMapper.treeToValue(errorsIterator.next(), ApiError.FieldError.class);
                        error.getErrors().add(fieldError);
                    }
                    return new HystrixBadRequestException(error.getMessage(), error);
            }
        } catch (IOException e) {
            log.error("", e);
        }

        return FeignException.errorStatus(methodKey, response);
    }

}
