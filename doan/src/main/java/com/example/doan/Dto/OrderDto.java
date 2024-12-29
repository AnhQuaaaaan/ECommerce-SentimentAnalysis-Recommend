package com.example.doan.Dto;

import com.example.doan.Entity.Customer;
import com.example.doan.Entity.Product;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderDto {
    private int id;
    private String customerName,customerPhone,deliveryAddress,note,email;
    private Date orderDate;
    private float totalAmount;
    private Customer customer;
}
