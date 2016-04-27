package com.ytripapp.api.client.feign.support;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class ApiRequestContextInterceptor implements RequestInterceptor {

    public void apply(RequestTemplate template) {
        ApiRequestContext context = ApiRequestContextHolder.instance().get();
        context.getHeaders().entrySet().stream().forEach(
                entry -> template.header(entry.getKey(), entry.getValue())
        );
        /*
        template.header(
                ApiRequestLocaleResolver.LOCALE_HEADER_NAME,
                context.getLocale().toString()
        );*/
        /*template.header(
                "Authorization",
                basicAuthHeaderValue()
        );*/
    }

    /*private String basicAuthHeaderValue() {
        ApiRequestContext context = ApiRequestContextHolder.instance().get();
        final String username = context.getUsername();
        final String password = context.getPassword();
        assert username != null;
        assert password != null;

        try {
            return "Basic " + encode(username, password);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private String encode(String username, String password) throws UnsupportedEncodingException {
        byte[] out = Base64.getEncoder().encode((username + ":" + password).getBytes(Util.ISO_8859_1));
        return new String(out, "US-ASCII");
    }*/
}
