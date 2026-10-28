package com.servicehub.servicehub_api.service;

import com.servicehub.servicehub_api.dto.ServiceRequestRequest;
import com.servicehub.servicehub_api.exception.ResourceNotFoundException;
import com.servicehub.servicehub_api.model.Role;
import com.servicehub.servicehub_api.model.ServiceRequest;
import com.servicehub.servicehub_api.model.usuario;
import com.servicehub.servicehub_api.repository.reviewRepository;
import com.servicehub.servicehub_api.repository.serviceRepository;
import com.servicehub.servicehub_api.Util.Sanitizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class serviceRequestService {

    @Autowired
    private serviceRepository serviceRepository;

    @Autowired
    private reviewRepository reviewRepository;

    @Autowired
    private usuarioService usuarioService;

    public List<ServiceRequest> listarTodos() {
        return serviceRepository.findAll();
    }

    // CLIENT vê só os próprios pedidos; ADMIN e PROVIDER veem todos
    public List<ServiceRequest> listarVisiveis(String emailLogado) {
        usuario logado = usuarioService.buscarPorEmail(emailLogado);
        if (logado.getRole() == Role.CLIENT) {
            return serviceRepository.findByUsuarioId(logado.getId());
        }
        return serviceRepository.findAll();
    }

    public ServiceRequest buscarPorId(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitação de serviço não encontrada"));
    }

    public ServiceRequest buscarPorId(Long id, String emailLogado) {
        ServiceRequest servico = buscarPorId(id);
        verificarAcesso(servico, emailLogado, true);
        return servico;
    }

    public ServiceRequest criar(ServiceRequestRequest dados, String emailLogado) {
        ServiceRequest novo = new ServiceRequest();
        novo.setTitle(Sanitizer.limpar(dados.getTitle()));
        novo.setDescription(Sanitizer.limpar(dados.getDescription()));
        novo.setStatus("PENDENTE");
        novo.setUsuario(usuarioService.buscarPorEmail(emailLogado));
        return serviceRepository.save(novo);
    }

    public ServiceRequest atualizar(Long id, ServiceRequestRequest dados, String emailLogado) {
        ServiceRequest servico = buscarPorId(id);
        verificarAcesso(servico, emailLogado, false);
        servico.setTitle(Sanitizer.limpar(dados.getTitle()));
        servico.setDescription(Sanitizer.limpar(dados.getDescription()));
        return serviceRepository.save(servico);
    }

    public ServiceRequest atualizarStatus(Long id, String status) {
        ServiceRequest servico = buscarPorId(id);
        servico.setStatus(status);
        return serviceRepository.save(servico);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        reviewRepository.findByServiceRequestId(id).ifPresent(reviewRepository::delete);
        serviceRepository.deleteById(id);
    }

    // Proteção contra BOLA: dono do pedido, ADMIN ou (quando permitido) PROVIDER
    private void verificarAcesso(ServiceRequest servico, String emailLogado, boolean providerPode) {
        usuario logado = usuarioService.buscarPorEmail(emailLogado);
        boolean ehAdmin = logado.getRole() == Role.ADMIN;
        boolean ehProvider = providerPode && logado.getRole() == Role.PROVIDER;
        boolean ehDono = servico.getUsuario() != null
                && servico.getUsuario().getId().equals(logado.getId());
        if (!ehAdmin && !ehProvider && !ehDono) {
            throw new AccessDeniedException("Acesso negado");
        }
    }
}