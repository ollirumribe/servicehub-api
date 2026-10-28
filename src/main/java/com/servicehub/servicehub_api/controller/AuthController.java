package com.servicehub.servicehub_api.controller;

import com.servicehub.servicehub_api.dto.LoginRequest;
import com.servicehub.servicehub_api.dto.LoginResponse;
import com.servicehub.servicehub_api.Security.JwtService;
import com.servicehub.servicehub_api.Util.Sanitizer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Autenticação", description = "Login e geração do token JWT")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Operation(summary = "Faz login e devolve um token JWT",
            description = "401 se e-mail ou senha estiverem errados")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest dados) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        Sanitizer.normalizarEmail(dados.getEmail()), dados.getPassword()));

        UserDetails user = (UserDetails) auth.getPrincipal();
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new LoginResponse(token, "Bearer", jwtService.getExpiration()));
    }
}