package com.servicehub.servicehub_api.dto;

import com.servicehub.servicehub_api.model.Review;
import lombok.Data;

@Data
public class ReviewResponse {

    private Long id;
    private Integer rating;
    private String comment;
    private Long serviceRequestId;
    private String serviceRequestTitle;

    public static ReviewResponse from(Review review) {
        ReviewResponse r = new ReviewResponse();
        r.setId(review.getId());
        r.setRating(review.getRating());
        r.setComment(review.getComment());
        if (review.getServiceRequest() != null) {
            r.setServiceRequestId(review.getServiceRequest().getId());
            r.setServiceRequestTitle(review.getServiceRequest().getTitle());
        }
        return r;
    }
}