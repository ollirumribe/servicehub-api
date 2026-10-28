package com.servicehub.servicehub_api.dto;

import com.servicehub.servicehub_api.model.ServiceRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Pedido de serviço (sem dados sensíveis do cliente)")
public class ServiceRequestResponse {

    private Long id;
    private String title;
    private String description;
    private String status;
    private LocalDateTime createdAt;
    private Long clienteId;
    private String clienteNome;

    public static ServiceRequestResponse from(ServiceRequest s) {
        ServiceRequestResponse r = new ServiceRequestResponse();
        r.setId(s.getId());
        r.setTitle(s.getTitle());
        r.setDescription(s.getDescription());
        r.setStatus(s.getStatus());
        r.setCreatedAt(s.getCreatedAt());
        if (s.getUsuario() != null) {
            r.setClienteId(s.getUsuario().getId());
            r.setClienteNome(s.getUsuario().getFullName());
        }
        return r;
    }
}