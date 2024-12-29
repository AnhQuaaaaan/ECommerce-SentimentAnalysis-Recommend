package com.example.doan.Dto;

import com.example.doan.Entity.Cart;
import com.example.doan.Entity.Product;
import lombok.Data;

@Data
public class CartItemsDto {
    private int id,quantity;
    private Product product;
    private float price;
    private Cart cart;
}
