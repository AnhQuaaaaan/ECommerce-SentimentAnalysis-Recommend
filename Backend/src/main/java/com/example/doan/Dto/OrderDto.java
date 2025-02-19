package com.example.doan.Dto;

import com.example.doan.Entity.User;
import lombok.Data;

import java.util.Date;

@Data
public class OrderDto {
    private int id;
    private String customerName,customerPhone,deliveryAddress,note,email;
    private Date orderDate;
    private float totalAmount;
    private User customer;
}
