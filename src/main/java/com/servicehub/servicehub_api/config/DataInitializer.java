package com.servicehub.servicehub_api.config;

import com.servicehub.servicehub_api.model.Role;
import com.servicehub.servicehub_api.model.usuario;
import com.servicehub.servicehub_api.repository.usuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner criarAdmin(usuarioRepository repository,
                                        PasswordEncoder passwordEncoder,
                                        @Value("${app.admin.email}") String email,
                                        @Value("${app.admin.password}") String senha) {
        return args -> {
            if (!repository.existsByEmail(email)) {
                usuario admin = new usuario();
                admin.setFullName("Administrador");
                admin.setEmail(email);
                admin.setPassword(passwordEncoder.encode(senha));
                admin.setRole(Role.ADMIN);
                repository.save(admin);
            }
        };
    }
}