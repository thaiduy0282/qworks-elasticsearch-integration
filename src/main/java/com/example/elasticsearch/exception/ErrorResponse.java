package com.example.elasticsearch.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class ErrorResponse {

    private int status;

    private String message;

}
