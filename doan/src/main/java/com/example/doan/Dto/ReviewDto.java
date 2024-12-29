package com.example.doan.Dto;

import com.example.doan.Entity.Customer;
import com.example.doan.Entity.Product;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
public class ReviewDto {
    private int id,rating,sentimentScore;
    private String comment;
    private Date createdAt;
    private Customer customer;
    private Product product;
}
