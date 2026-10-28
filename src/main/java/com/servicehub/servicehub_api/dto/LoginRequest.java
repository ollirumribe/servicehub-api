package com.servicehub.servicehub_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Credenciais de login")
public class LoginRequest {

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Schema(example = "admin@servicehub.com")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Schema(example = "Admin@123")
    private String password;
}