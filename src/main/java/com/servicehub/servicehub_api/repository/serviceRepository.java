package com.servicehub.servicehub_api.repository;

import com.servicehub.servicehub_api.model.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface serviceRepository extends JpaRepository<ServiceRequest, Long> {
}
