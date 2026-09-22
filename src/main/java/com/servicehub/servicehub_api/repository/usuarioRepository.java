package com.servicehub.servicehub_api.repository;

import com.servicehub.servicehub_api.model.usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface usuarioRepository extends JpaRepository<usuario, Long> {
}
