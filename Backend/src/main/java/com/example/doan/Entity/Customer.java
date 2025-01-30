package com.example.doan.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "`customer`")
public class Customer {
    @Id
    private String id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String email;

    private String fullname;
    @Temporal(TemporalType.DATE)
    private Date dob;
    @Column(length = 10)
    private String phone;

    private String address;
}
