package com.servicehub.servicehub_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "Novo status do pedido")
public class ServiceRequestStatusRequest {

    @NotBlank(message = "O status é obrigatório")
    @Pattern(regexp = "PENDENTE|EM_ANDAMENTO|CONCLUIDO|CANCELADO",
            message = "Status deve ser PENDENTE, EM_ANDAMENTO, CONCLUIDO ou CANCELADO")
    @Schema(example = "CONCLUIDO")
    private String status;
}