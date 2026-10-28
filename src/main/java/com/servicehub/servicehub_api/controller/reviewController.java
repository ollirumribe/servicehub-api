package com.servicehub.servicehub_api.controller;

import com.servicehub.servicehub_api.dto.ReviewRequest;
import com.servicehub.servicehub_api.dto.ReviewResponse;
import com.servicehub.servicehub_api.dto.ReviewUpdateRequest;
import com.servicehub.servicehub_api.service.reviewService;
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

@Tag(name = "Avaliações", description = "Avaliações de serviços concluídos")
@RestController
@RequestMapping("/api/reviews")
public class reviewController {

    @Autowired
    private reviewService reviewService;

    @Operation(summary = "Lista as avaliações")
    @GetMapping
    public List<ReviewResponse> listarTodos() {
        return reviewService.listarTodos().stream().map(ReviewResponse::from).toList();
    }

    @Operation(summary = "Busca uma avaliação pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ReviewResponse.from(reviewService.buscarPorId(id)));
    }

    @Operation(summary = "Avalia um serviço concluído (somente o CLIENT dono do pedido)")
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping
    public ResponseEntity<ReviewResponse> criar(@Valid @RequestBody ReviewRequest dados,
                                                Authentication authentication) {
        ReviewResponse nova = ReviewResponse.from(reviewService.criar(dados, authentication.getName()));
        return ResponseEntity.status(HttpStatus.CREATED).body(nova);
    }

    @Operation(summary = "Edita uma avaliação (autor ou ADMIN)")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> atualizar(@PathVariable Long id,
                                                    @Valid @RequestBody ReviewUpdateRequest dados,
                                                    Authentication authentication) {
        return ResponseEntity.ok(ReviewResponse.from(
                reviewService.atualizar(id, dados, authentication.getName())));
    }

    @Operation(summary = "Remove uma avaliação (somente ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        reviewService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}