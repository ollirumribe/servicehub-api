package com.servicehub.servicehub_api.service;

import com.servicehub.servicehub_api.dto.usuarioRequest;
import com.servicehub.servicehub_api.dto.usuarioUpdateRequest;
import com.servicehub.servicehub_api.exception.BusinessException;
import com.servicehub.servicehub_api.exception.ResourceNotFoundException;
import com.servicehub.servicehub_api.model.Role;
import com.servicehub.servicehub_api.Util.Sanitizer;
import com.servicehub.servicehub_api.model.usuario;
import org.springframework.security.access.AccessDeniedException;
import com.servicehub.servicehub_api.repository.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class usuarioService {

    @Autowired
    private usuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    public usuario criar(usuarioRequest dados) {
        String email = Sanitizer.normalizarEmail(dados.getEmail());
        if (usuarioRepository.existsByEmail(email)) {
            throw new BusinessException("E-mail já cadastrado");
        }
        if (dados.getRole() == Role.ADMIN) {
            throw new BusinessException("Não é permitido se cadastrar como ADMIN");
        }
        usuario novo = new usuario();
        novo.setFullName(Sanitizer.limpar(dados.getFullName()));
        novo.setEmail(email);
        novo.setPassword(passwordEncoder.encode(dados.getPassword()));
        novo.setRole(dados.getRole() == null ? Role.CLIENT : dados.getRole());
        return usuarioRepository.save(novo);
    }

    public usuario atualizar(Long id, usuarioUpdateRequest dados) {
        usuario existente = buscarPorId(id);
        String email = Sanitizer.normalizarEmail(dados.getEmail());
        if (!email.equals(existente.getEmail()) && usuarioRepository.existsByEmail(email)) {
            throw new BusinessException("E-mail já cadastrado");
        }
        existente.setFullName(Sanitizer.limpar(dados.getFullName()));
        existente.setEmail(email);
        return usuarioRepository.save(existente);
    }
    public usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    // Proteção contra BOLA: só o próprio usuário ou um ADMIN acessa os dados
    public void verificarAcesso(Long id, String emailLogado) {
        usuario logado = buscarPorEmail(emailLogado);
        if (logado.getRole() != Role.ADMIN && !logado.getId().equals(id)) {
            throw new AccessDeniedException("Acesso negado");
        }
    }

    public void deletar(Long id) {
        buscarPorId(id);
        usuarioRepository.deleteById(id);
    }
}