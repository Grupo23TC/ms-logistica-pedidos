package com.fiap.tc.logistica.helper;


import feign.*;
import feign.Request.HttpMethod;
import org.springframework.http.HttpStatus;
import feign.Request;

import java.util.Collections;

public class FeignExceptionHelper {

    public static FeignException gerarFeignExceptionNotFound() {
        // Create a mock Response with 404 status code
        Response response = Response.builder()
                .status(HttpStatus.NOT_FOUND.value())  // 404 status code
                .reason("Not Found")                   // Error message
                .request(Request.create(HttpMethod.GET, "https://example.com", Collections.emptyMap(), null, new RequestTemplate())) // Request
                .build();

        // Return a FeignException using the Response object
        return FeignException.errorStatus("GET", response);
    }

    public static FeignException gerarFeignExceptionInternalServerError() {
        // Create a mock Response with 500 status code
        Response response = Response.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())  // 500 status code
                .reason("Internal Server Error")                   // Error message
                .request(Request.create(HttpMethod.GET, "https://example.com", Collections.emptyMap(), null, new RequestTemplate())) // Request
                .build();

        // Return a FeignException using the Response object
        return FeignException.errorStatus("GET", response);
    }
}
