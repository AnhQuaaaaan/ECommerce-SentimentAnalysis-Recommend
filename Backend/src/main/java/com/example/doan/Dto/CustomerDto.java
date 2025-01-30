package com.example.doan.Dto;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.util.Date;

@Data
public class CustomerDto {
    private String id,username,password,fullname,phone,address,email;
    private Date dob;
}
