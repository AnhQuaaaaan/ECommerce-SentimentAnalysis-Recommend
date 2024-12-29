package com.example.doan.Service;

import com.example.doan.Dto.ReviewDto;

import java.util.List;

public interface ReviewService {
    List<ReviewDto> getReviewsByProduct(String id);
    void saveReview(ReviewDto reviewDto);
    void updateReview(int id,ReviewDto reviewDto);
    void deleteReview(int id);
}
