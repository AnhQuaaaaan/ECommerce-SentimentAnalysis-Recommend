package com.example.doan.Entity;

import com.example.doan.Entity.num.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "`user`")
public class User {
    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(nullable = false)
    private String email;

    private String fullname;
    @Temporal(TemporalType.DATE)
    private Date dob;
    @Column(length = 10)
    private String phone;

    private String address;
}
