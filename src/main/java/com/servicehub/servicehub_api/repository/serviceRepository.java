package com.servicehub.servicehub_api.repository;

import com.servicehub.servicehub_api.model.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface serviceRepository extends JpaRepository<ServiceRequest, Long> {

    List<ServiceRequest> findByUsuarioId(Long usuarioId);
}