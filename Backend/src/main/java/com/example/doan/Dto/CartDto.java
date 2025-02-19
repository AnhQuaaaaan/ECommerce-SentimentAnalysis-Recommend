package com.example.doan.Dto;

import com.example.doan.Entity.User;
import lombok.Data;

@Data
public class CartDto {
    private int id;
    private User customer;

}
