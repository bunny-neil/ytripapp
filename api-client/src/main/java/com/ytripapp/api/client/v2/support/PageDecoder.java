package com.ytripapp.api.client.v2.support;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ytripapp.api.client.feign.decoder.CustomObjectDecoder;
import feign.Response;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PageDecoder implements CustomObjectDecoder {

    ObjectMapper objectMapper;

    public PageDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean canRead(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Class<?> rawType = (Class<?>) parameterizedType.getRawType();
            return Page.class.isAssignableFrom(rawType);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object decode(Response response, Type type) throws IOException {
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Class<?> paramType = (Class<?>) parameterizedType.getActualTypeArguments()[0];

        JsonNode rootNode = objectMapper.readTree(response.body().asInputStream());
        int totalCount = rootNode.get("totalCount").asInt();

        JsonNode pageRequestNode = rootNode.get("pageRequest");
        PageRequest pageRequest = objectMapper.treeToValue(pageRequestNode, PageRequest.class);

        Iterator<JsonNode> contentIterator = rootNode.get("content").elements();
        List contentObjects = new ArrayList<>();
        while (contentIterator.hasNext()) {
            Object obj = objectMapper.treeToValue(contentIterator.next(), paramType);
            contentObjects.add(obj);
        }
        return new Page<>(contentObjects, totalCount, pageRequest);
    }
}
