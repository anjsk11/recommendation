package com.sensingbros.recommendation.model;

import lombok.Getter;

@Getter
public class ResponseDTO<T> {
    private boolean success_code;
    private T data;

    public ResponseDTO(boolean successCode) {
        this.success_code = successCode;
    }

    public ResponseDTO(boolean successCode, T data) {
        this.success_code = successCode;
        this.data = data;
    }
}
