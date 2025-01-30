package com.example.doan.Dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ProductDto {
    private String id,name,description,image,details;
    private float price;
}
