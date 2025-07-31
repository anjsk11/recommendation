package com.sensingbros.recommendation.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UsersDTO {
    @NotNull
    private String idToken;
}
