package com.example.doan.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Temporal(TemporalType.DATE)
    private Date orderDate;
    @Column(nullable = false)
    private float totalAmount;
    @ManyToOne
    @JoinColumn(name = "customer_id",nullable = false)
    private User customerorder;
    @Column(nullable = false)
    private String customerName;
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String customerPhone;

    @Column(nullable = false)
    private String deliveryAddress;
    @Column
    private String note;
}
