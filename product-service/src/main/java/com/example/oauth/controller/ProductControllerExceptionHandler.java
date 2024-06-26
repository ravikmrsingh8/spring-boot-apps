package com.example.oauth.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestControllerAdvice
public class ProductControllerExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ProblemDetail handleNotFoundException(ProductNotFoundException ex) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        //URI Points to documentation of the custom error
        URI uri = UriComponentsBuilder.fromHttpUrl("https://examples.net/validation_error").build().toUri();
        detail.setType(uri);

        detail.setDetail(ex.getMessage());
        return detail;
    }
}
