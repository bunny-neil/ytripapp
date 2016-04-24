package com.ytripapp.api.client.feign.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.ytripapp.api.client.feign.support.ApiError;
import feign.FeignException;
import feign.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class FeignErrorDecoder implements feign.codec.ErrorDecoder {

    ObjectMapper objectMapper;

    public FeignErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            switch (response.status()) {
                case 400:
                case 404:
                case 500:
                    ApiError error = objectMapper.readValue(response.body().asInputStream(), ApiError.class);
                    return new HystrixBadRequestException(error.getMessage(), error);
            }
        } catch (IOException e) {
            log.error("", e);
        }

        return FeignException.errorStatus(methodKey, response);
    }

}
