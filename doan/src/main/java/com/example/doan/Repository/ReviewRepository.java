package com.example.doan.Repository;

import com.example.doan.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository  extends JpaRepository<Review, Integer> {
    List<Review> getReviewsByProduct_Id(String id);
}
