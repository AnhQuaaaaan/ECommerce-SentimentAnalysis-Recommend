package com.example.doan.Service.Impl;

import com.example.doan.Dto.ProductDto;
import com.example.doan.Dto.ReviewDto;
import com.example.doan.Entity.Product;
import com.example.doan.Entity.Review;
import com.example.doan.Repository.ReviewRepository;
import com.example.doan.Service.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    @Value("${sentiment.api.url}")
    private String sentimentApiUrl;
    @Override
    public List<ReviewDto> getReviewsByProduct(String id) {
        List<Review> reviews=reviewRepository.getReviewsByProduct_Id(id);
        List<ReviewDto> reviewDtos = reviews.stream()
                .map(this::convertToReviewDTO)
                .collect(Collectors.toList());
        return reviewDtos;
    }

    @Override
    public void saveReview(ReviewDto reviewDto) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("text", reviewDto.getComment());

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody);
        try {
            ResponseEntity<Integer> response = restTemplate.postForEntity(sentimentApiUrl, request, Integer.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                int sentimentScore = response.getBody();
                reviewDto.setSentimentScore(sentimentScore);
            } else {
                throw new RuntimeException("Failed to get sentiment score from Python API");
            }
        } catch (Exception e) {
            System.err.println("Error calling sentiment API: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error calling sentiment API", e);
        }

        Review review = convertToReview(reviewDto);
        reviewRepository.save(review);
    }

    @Override
    public void updateReview(int id,ReviewDto reviewDto) {

        try {
            Optional<Review> reviewOptional=reviewRepository.findById(id);
            Review review=reviewOptional.get();
            review.setRating(reviewDto.getRating());
            review.setComment(reviewDto.getComment());
            review.setCreatedAt(reviewDto.getCreatedAt());
            RestTemplate restTemplate = new RestTemplate();
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("text", reviewDto.getComment());

            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody);
            try {
                ResponseEntity<Integer> response = restTemplate.postForEntity(sentimentApiUrl, request, Integer.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    int sentimentScore = response.getBody();
                    review.setSentimentScore(sentimentScore);
                } else {
                    throw new RuntimeException("Failed to get sentiment score from Python API");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error calling sentiment API", e);
            }
            reviewRepository.save(review);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteReview(int id) {
        try {
            Optional<Review> reviewOptional = reviewRepository.findById(id);
                Review review = reviewOptional.get();
                reviewRepository.delete(review);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ReviewDto convertToReviewDTO(Review review) {
        ReviewDto reviewDto=new ReviewDto();
        reviewDto.setId(review.getId());
        reviewDto.setComment(review.getComment());
        reviewDto.setProduct(review.getProduct());
        reviewDto.setCustomer(review.getCustomer());
        reviewDto.setRating(review.getRating());
        reviewDto.setCreatedAt(review.getCreatedAt());
        reviewDto.setSentimentScore(review.getSentimentScore());
        return reviewDto;
    }
    private Review convertToReview(ReviewDto ReviewDto) {
        Review review=new Review();
        review.setComment(ReviewDto.getComment());
        review.setProduct(ReviewDto.getProduct());
        review.setCustomer(ReviewDto.getCustomer());
        review.setRating(ReviewDto.getRating());
        review.setCreatedAt(ReviewDto.getCreatedAt());
        review.setSentimentScore(ReviewDto.getSentimentScore());
        return review;
    }
}
