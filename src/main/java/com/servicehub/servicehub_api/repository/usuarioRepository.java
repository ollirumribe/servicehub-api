package com.servicehub.servicehub_api.repository;

import com.servicehub.servicehub_api.model.usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface usuarioRepository extends JpaRepository<usuario, Long> {

    Optional<usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}