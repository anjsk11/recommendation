package com.sensingbros.recommendation.model;

public class TFResponseDTO {
    private boolean success_code;

    public TFResponseDTO() {
    }

    public TFResponseDTO(boolean success_code) {
        this.success_code = success_code;
    }

    public boolean isSuccess_code() {
        return success_code;
    }

    public void setSuccess_code(boolean success_code) {
        this.success_code = success_code;
    }
}
