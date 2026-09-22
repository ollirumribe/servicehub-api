package com.servicehub.servicehub_api.controller;

import com.servicehub.servicehub_api.model.ServiceRequest;
import com.servicehub.servicehub_api.service.serviceRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-requests")
public class ServiceRequestController {

    @Autowired
    private serviceRequestService serviceRequestService;

    @GetMapping
    public List<ServiceRequest> listarTodos() {
        return serviceRequestService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequest> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(serviceRequestService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ServiceRequest> criar(@RequestBody ServiceRequest serviceRequest) {
        ServiceRequest novo = serviceRequestService.criar(serviceRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceRequest> atualizar(@PathVariable Long id, @RequestBody ServiceRequest serviceRequest) {
        return ResponseEntity.ok(serviceRequestService.atualizar(id, serviceRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        serviceRequestService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}