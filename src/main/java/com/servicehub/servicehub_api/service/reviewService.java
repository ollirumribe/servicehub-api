package com.servicehub.servicehub_api.service;

import com.servicehub.servicehub_api.exception.ResourceNotFoundException;
import com.servicehub.servicehub_api.model.Review;
import com.servicehub.servicehub_api.repository.reviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class reviewService {

    @Autowired
    private reviewRepository reviewRepository;

    public List<Review> listarTodos() {
        return reviewRepository.findAll();
    }

    public Review buscarPorId(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada"));
    }

    public Review criar(Review review) {
        return reviewRepository.save(review);
    }

    public Review atualizar(Long id, Review dadosNovos) {
        Review review = buscarPorId(id);
        review.setRating(dadosNovos.getRating());
        review.setComment(dadosNovos.getComment());
        return reviewRepository.save(review);
    }

    public void deletar(Long id) {
        reviewRepository.deleteById(id);
    }
}