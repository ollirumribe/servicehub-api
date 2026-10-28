package com.servicehub.servicehub_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Avaliação de um serviço concluído")
public class ReviewRequest {

    @NotNull(message = "O pedido de serviço é obrigatório")
    @Schema(example = "1")
    private Long serviceRequestId;

    @NotNull(message = "A nota é obrigatória")
    @Min(value = 1, message = "A nota mínima é 1")
    @Max(value = 5, message = "A nota máxima é 5")
    @Schema(example = "5")
    private Integer rating;

    @Size(max = 500, message = "O comentário deve ter no máximo 500 caracteres")
    @Schema(example = "Ótimo atendimento")
    private String comment;
}