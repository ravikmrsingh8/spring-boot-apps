package com.example.feign.config.feign;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class ProductApiErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        System.out.println("Method Key" + methodKey);
        Response.Body body = response.body();
        HttpStatus status = HttpStatus.valueOf(response.status());
        if (status.is4xxClientError()) {
            return new RuntimeException("401 UnAuthorized");
        }
        return new RuntimeException("500 Internal Server Error");
    }
}
