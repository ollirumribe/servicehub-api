package com.servicehub.servicehub_api.controller;

import com.servicehub.servicehub_api.dto.usuarioRequest;
import com.servicehub.servicehub_api.dto.usuarioResponse;
import com.servicehub.servicehub_api.dto.usuarioUpdateRequest;
import com.servicehub.servicehub_api.service.usuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuários", description = "Cadastro e consulta de usuários do ServiceHub")
@RestController
@RequestMapping("/api/users")
public class usuarioController {

    @Autowired
    private usuarioService usuarioService;

    @Operation(summary = "Lista todos os usuários")
    @GetMapping
    public List<usuarioResponse> listarTodos() {
        return usuarioService.listarTodos().stream().map(usuarioResponse::from).toList();
    }

    @Operation(summary = "Busca um usuário pelo ID", description = "Retorna 404 se o usuário não existir")
    @GetMapping("/{id}")
    public ResponseEntity<usuarioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioResponse.from(usuarioService.buscarPorId(id)));
    }

    @Operation(summary = "Cadastra um usuário", description = "A senha é armazenada com hash BCrypt e nunca é devolvida")
    @PostMapping
    public ResponseEntity<usuarioResponse> criar(@Valid @RequestBody usuarioRequest dados) {
        usuarioResponse criado = usuarioResponse.from(usuarioService.criar(dados));
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @Operation(summary = "Atualiza nome e e-mail de um usuário")
    @PutMapping("/{id}")
    public ResponseEntity<usuarioResponse> atualizar(@PathVariable Long id,
                                                     @Valid @RequestBody usuarioUpdateRequest dados) {
        return ResponseEntity.ok(usuarioResponse.from(usuarioService.atualizar(id, dados)));
    }

    @Operation(summary = "Remove um usuário")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}