package com.example.doan.Dto;

import com.example.doan.Entity.num.Role;
import lombok.Data;

import java.util.Date;

@Data
public class UserDto {
    private String id,username,password,fullname,phone,address,email;
    private Date dob;
    private Role role;
}
