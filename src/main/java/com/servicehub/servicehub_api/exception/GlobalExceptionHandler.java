package com.servicehub.servicehub_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 - recurso não existe
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        return resposta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // 400 - Bean Validation (@Valid) falhou
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> campos = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> campos.put(e.getField(), e.getDefaultMessage()));

        ResponseEntity<Map<String, Object>> resposta = resposta(HttpStatus.BAD_REQUEST, "Dados inválidos");
        resposta.getBody().put("campos", campos);
        return resposta;
    }

    // 400 - JSON mal formatado ou valor inválido (ex.: role "XYZ")
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleJsonInvalido(HttpMessageNotReadableException ex) {
        return resposta(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido ou mal formatado");
    }

    // 409 - regra de negócio (e-mail duplicado, cadastro como ADMIN)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleNegocio(BusinessException ex) {
        return resposta(HttpStatus.CONFLICT, ex.getMessage());
    }

    // 401 - login com e-mail ou senha errados
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleLoginInvalido(BadCredentialsException ex) {
        return resposta(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos");
    }

    // 403 - @PreAuthorize negou ou usuário tentou acessar recurso de outro
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAcessoNegado(AccessDeniedException ex) {
        return resposta(HttpStatus.FORBIDDEN, "Acesso negado para o seu perfil");
    }

    private ResponseEntity<Map<String, Object>> resposta(HttpStatus status, String mensagem) {
        Map<String, Object> erro = new LinkedHashMap<>();
        erro.put("timestamp", LocalDateTime.now());
        erro.put("status", status.value());
        erro.put("mensagem", mensagem);
        return ResponseEntity.status(status).body(erro);
    }
}