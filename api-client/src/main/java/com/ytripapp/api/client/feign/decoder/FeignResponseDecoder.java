package com.ytripapp.api.client.feign.decoder;

import feign.FeignException;
import feign.Response;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.support.SpringDecoder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class FeignResponseDecoder extends SpringDecoder {

    List<? extends CustomObjectDecoder> decoders;

    public FeignResponseDecoder(List<? extends CustomObjectDecoder> decoders,
                                ObjectFactory<HttpMessageConverters> messageConverters) {
        super(messageConverters);
        this.decoders = decoders;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        for (CustomObjectDecoder decoder : decoders) {
            if (decoder.canRead(type)) {
                return decoder.decode(response, type);
            }
        }
        return super.decode(response, type);
    }
}
