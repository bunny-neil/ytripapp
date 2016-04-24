package com.ytripapp.api.client.feign.decoder;

import feign.Response;

import java.io.IOException;
import java.lang.reflect.Type;

public interface CustomObjectDecoder {

    boolean canRead(Type type);

    Object decode(Response response, Type type) throws IOException;

}
