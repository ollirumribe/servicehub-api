package com.servicehub.servicehub_api.repository;

import com.servicehub.servicehub_api.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface reviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByServiceRequestId(Long serviceRequestId);

    boolean existsByServiceRequestId(Long serviceRequestId);
}