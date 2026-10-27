package com.servicehub.servicehub_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Dados para atualização de usuário")
public class usuarioUpdateRequest {

    @NotBlank(message = "O nome completo é obrigatório")
    @Schema(description = "Nome completo do usuário", example = "Maria Souza Silva")
    private String fullName;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Schema(description = "E-mail do usuário", example = "maria@servicehub.com")
    private String email;
}