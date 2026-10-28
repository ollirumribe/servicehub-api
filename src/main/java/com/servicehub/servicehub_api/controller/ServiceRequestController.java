package com.servicehub.servicehub_api.controller;

import com.servicehub.servicehub_api.dto.ServiceRequestRequest;
import com.servicehub.servicehub_api.dto.ServiceRequestResponse;
import com.servicehub.servicehub_api.dto.ServiceRequestStatusRequest;
import com.servicehub.servicehub_api.service.serviceRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Pedidos de serviço", description = "Solicitações de serviço feitas por clientes")
@RestController
@RequestMapping("/api/service-requests")
public class ServiceRequestController {

    @Autowired
    private serviceRequestService serviceRequestService;

    @Operation(summary = "Lista pedidos (CLIENT vê só os seus)")
    @GetMapping
    public List<ServiceRequestResponse> listar(Authentication authentication) {
        return serviceRequestService.listarVisiveis(authentication.getName())
                .stream().map(ServiceRequestResponse::from).toList();
    }

    @Operation(summary = "Busca um pedido pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequestResponse> buscarPorId(@PathVariable Long id,
                                                              Authentication authentication) {
        return ResponseEntity.ok(ServiceRequestResponse.from(
                serviceRequestService.buscarPorId(id, authentication.getName())));
    }

    @Operation(summary = "Cria um pedido (CLIENT ou ADMIN)")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    @PostMapping
    public ResponseEntity<ServiceRequestResponse> criar(@Valid @RequestBody ServiceRequestRequest dados,
                                                        Authentication authentication) {
        ServiceRequestResponse novo = ServiceRequestResponse.from(
                serviceRequestService.criar(dados, authentication.getName()));
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    @Operation(summary = "Edita título e descrição (dono do pedido ou ADMIN)")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ServiceRequestResponse> atualizar(@PathVariable Long id,
                                                            @Valid @RequestBody ServiceRequestRequest dados,
                                                            Authentication authentication) {
        return ResponseEntity.ok(ServiceRequestResponse.from(
                serviceRequestService.atualizar(id, dados, authentication.getName())));
    }

    @Operation(summary = "Muda o status (PROVIDER ou ADMIN)")
    @PreAuthorize("hasAnyRole('PROVIDER', 'ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ServiceRequestResponse> atualizarStatus(@PathVariable Long id,
                                                                  @Valid @RequestBody ServiceRequestStatusRequest dados) {
        return ResponseEntity.ok(ServiceRequestResponse.from(
                serviceRequestService.atualizarStatus(id, dados.getStatus())));
    }

    @Operation(summary = "Remove um pedido (somente ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        serviceRequestService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}