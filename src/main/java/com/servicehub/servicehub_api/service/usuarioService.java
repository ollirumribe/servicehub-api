package com.servicehub.servicehub_api.service;

import com.servicehub.servicehub_api.dto.usuarioRequest;
import com.servicehub.servicehub_api.dto.usuarioUpdateRequest;
import com.servicehub.servicehub_api.exception.ResourceNotFoundException;
import com.servicehub.servicehub_api.model.usuario;
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
        usuario novo = new usuario();
        novo.setFullName(dados.getFullName());
        novo.setEmail(dados.getEmail());
        novo.setPassword(passwordEncoder.encode(dados.getPassword()));
        return usuarioRepository.save(novo);
    }

    public usuario atualizar(Long id, usuarioUpdateRequest dados) {
        usuario existente = buscarPorId(id);
        existente.setFullName(dados.getFullName());
        existente.setEmail(dados.getEmail());
        return usuarioRepository.save(existente);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        usuarioRepository.deleteById(id);
    }
}