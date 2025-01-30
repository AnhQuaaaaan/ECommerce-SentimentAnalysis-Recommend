package com.example.doan.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "`orderitems`")
public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private float price;
    @ManyToOne
    @JoinColumn(name = "product_id",nullable = false)
    private Product productitems;
    @ManyToOne
    @JoinColumn(name = "order_id",nullable = false)
    private Order order;
    @Column(nullable = false)
    private boolean hasReviewed;
}
