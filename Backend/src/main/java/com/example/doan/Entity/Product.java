package com.example.doan.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "`product`")
public class Product {
    @Id
    private String id;
    @Column(nullable = false)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(nullable = false)
    private float price;
    @Column(nullable = false)
    private String image;
    @Column(columnDefinition = "TEXT")
    private String details;
}
