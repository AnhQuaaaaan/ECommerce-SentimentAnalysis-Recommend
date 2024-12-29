package com.example.doan.Dto;

import com.example.doan.Entity.Order;
import com.example.doan.Entity.Product;
import lombok.Data;

import java.util.List;

@Data
public class OrderItemsDto {
    private int id,quantity;
    private float price;
    private Product product;
    private Order order;
    private boolean hasReviewed;
}
