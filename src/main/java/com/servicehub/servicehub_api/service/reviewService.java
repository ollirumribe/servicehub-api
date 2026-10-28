package com.servicehub.servicehub_api.service;

import com.servicehub.servicehub_api.dto.ReviewRequest;
import com.servicehub.servicehub_api.dto.ReviewUpdateRequest;
import com.servicehub.servicehub_api.exception.BusinessException;
import com.servicehub.servicehub_api.exception.ResourceNotFoundException;
import com.servicehub.servicehub_api.model.Review;
import com.servicehub.servicehub_api.model.Role;
import com.servicehub.servicehub_api.model.ServiceRequest;
import com.servicehub.servicehub_api.model.usuario;
import com.servicehub.servicehub_api.repository.reviewRepository;
import com.servicehub.servicehub_api.Util.Sanitizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class reviewService {

    @Autowired
    private reviewRepository reviewRepository;

    @Autowired
    private serviceRequestService serviceRequestService;

    @Autowired
    private usuarioService usuarioService;

    public List<Review> listarTodos() {
        return reviewRepository.findAll();
    }

    public Review buscarPorId(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada"));
    }

    public Review criar(ReviewRequest dados, String emailLogado) {
        ServiceRequest servico = serviceRequestService.buscarPorId(dados.getServiceRequestId());
        usuario logado = usuarioService.buscarPorEmail(emailLogado);

        if (servico.getUsuario() == null || !servico.getUsuario().getId().equals(logado.getId())) {
            throw new AccessDeniedException("Só o cliente que pediu o serviço pode avaliá-lo");
        }
        if (!"CONCLUIDO".equals(servico.getStatus())) {
            throw new BusinessException("Só é possível avaliar serviços concluídos");
        }
        if (reviewRepository.existsByServiceRequestId(servico.getId())) {
            throw new BusinessException("Este serviço já foi avaliado");
        }

        Review review = new Review();
        review.setRating(dados.getRating());
        review.setComment(Sanitizer.limpar(dados.getComment()));
        review.setServiceRequest(servico);
        return reviewRepository.save(review);
    }

    public Review atualizar(Long id, ReviewUpdateRequest dados, String emailLogado) {
        Review review = buscarPorId(id);
        usuario logado = usuarioService.buscarPorEmail(emailLogado);
        boolean ehAutor = review.getServiceRequest().getUsuario().getId().equals(logado.getId());
        if (logado.getRole() != Role.ADMIN && !ehAutor) {
            throw new AccessDeniedException("Acesso negado");
        }
        review.setRating(dados.getRating());
        review.setComment(Sanitizer.limpar(dados.getComment()));
        return reviewRepository.save(review);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        reviewRepository.deleteById(id);
    }
}