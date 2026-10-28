package com.servicehub.servicehub_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Token JWT devolvido no login")
public class LoginResponse {

    private String token;
    private String tipo;
    private long expiraEmMs;
}