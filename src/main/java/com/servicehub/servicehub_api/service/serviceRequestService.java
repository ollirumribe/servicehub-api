package com.servicehub.servicehub_api.service;

import com.servicehub.servicehub_api.exception.ResourceNotFoundException;
import com.servicehub.servicehub_api.model.ServiceRequest;
import com.servicehub.servicehub_api.repository.serviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class serviceRequestService {

    @Autowired
    private serviceRepository serviceRepository;

    public List<ServiceRequest> listarTodos() {
        return serviceRepository.findAll();
    }

    public ServiceRequest buscarPorId(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitação de serviço não encontrada"));
    }

    public ServiceRequest criar(ServiceRequest serviceRequest) {
        return serviceRepository.save(serviceRequest);
    }

    public ServiceRequest atualizar(Long id, ServiceRequest dadosNovos) {
        ServiceRequest servico = buscarPorId(id);
        servico.setTitle(dadosNovos.getTitle());
        servico.setDescription(dadosNovos.getDescription());
        servico.setStatus(dadosNovos.getStatus());
        return serviceRepository.save(servico);
    }

    public void deletar(Long id) {
        serviceRepository.deleteById(id);
    }
}