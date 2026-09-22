package com.servicehub.servicehub_api.service;

import com.servicehub.servicehub_api.model.usuario;
import com.servicehub.servicehub_api.model.usuario;
import com.servicehub.servicehub_api.exception.ResourceNotFoundException;
import com.servicehub.servicehub_api.repository.usuarioRepository;
import com.servicehub.servicehub_api.repository.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class usuarioService {

    @Autowired
    private usuarioRepository usuarioRepository;

    public List<usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    public usuario criar(usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public usuario atualizar(Long id, usuario dadosNovos) {
        usuario usuario = buscarPorId(id);
        usuario.setFullName(dadosNovos.getFullName());
        usuario.setEmail(dadosNovos.getEmail( ));
        return usuarioRepository.save(usuario);
    }

    public void deletar(Long id) {
        usuarioRepository.deleteById(id);
    }
}
