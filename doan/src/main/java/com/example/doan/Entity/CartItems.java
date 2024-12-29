package com.example.doan.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "`cartitems`")
public class CartItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer quantity;
    private Float price;

    @ManyToOne
    @JoinColumn(name = "cartId",nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "productId",nullable = false)
    private Product productcarts;
}
