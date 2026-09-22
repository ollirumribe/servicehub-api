package com.servicehub.servicehub_api.controller;

import com.servicehub.servicehub_api.model.Review;
import com.servicehub.servicehub_api.service.reviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class reviewController {

    @Autowired
    private reviewService reviewService;

    @GetMapping
    public List<Review> listarTodos() {
        return reviewService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Review> criar(@RequestBody Review review) {
        Review novo = reviewService.criar(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> atualizar(@PathVariable Long id, @RequestBody Review review) {
        return ResponseEntity.ok(reviewService.atualizar(id, review));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        reviewService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}