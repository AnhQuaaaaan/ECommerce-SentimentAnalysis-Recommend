package com.example.doan.Dto;

import com.example.doan.Entity.Customer;
import com.example.doan.Entity.Product;
import lombok.Data;

@Data
public class CartDto {
    private int id;
    private Customer customer;

}
