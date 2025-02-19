package com.example.doan.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "`review`")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private int rating;
    @Column(nullable = false,columnDefinition = "TEXT")
    private String comment;
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    @Column(nullable = false)
    private int sentimentScore;
    @ManyToOne
    @JoinColumn(name = "customer_id",nullable = false)
    private User customer;
    @ManyToOne
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;
}
