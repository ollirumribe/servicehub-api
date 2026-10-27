package com.servicehub.servicehub_api.dto;

import com.servicehub.servicehub_api.model.usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Dados públicos do usuário (nunca inclui a senha)")
public class usuarioResponse {

    @Schema(example = "1")
    private Long id;
    @Schema(example = "Maria Silva")
    private String fullName;
    @Schema(example = "maria@servicehub.com")
    private String email;
    private LocalDateTime createdAt;

    public static usuarioResponse from(usuario u) {
        usuarioResponse r = new usuarioResponse();
        r.setId(u.getId());
        r.setFullName(u.getFullName());
        r.setEmail(u.getEmail());
        r.setCreatedAt(u.getCreatedAt());
        return r;
    }
}