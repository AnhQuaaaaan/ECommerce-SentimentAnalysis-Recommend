package com.example.doan.Dto;

import com.example.doan.Entity.User;
import com.example.doan.Entity.Product;
import lombok.Data;

import java.util.Date;

@Data
public class ReviewDto {
    private int id,rating,sentimentScore;
    private String comment;
    private Date createdAt;
    private User customer;
    private Product product;
}
