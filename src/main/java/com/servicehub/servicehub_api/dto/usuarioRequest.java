package com.servicehub.servicehub_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Dados para cadastro de usuário")
public class usuarioRequest {

    @NotBlank(message = "O nome completo é obrigatório")
    @Schema(description = "Nome completo do usuário", example = "Maria Silva")
    private String fullName;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Schema(description = "E-mail do usuário", example = "maria@servicehub.com")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
    @Schema(description = "Senha com no mínimo 8 caracteres", example = "senha1234")
    private String password;
}