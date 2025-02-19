package com.example.doan.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
@Data
@AllArgsConstructor
public class JwtResponse implements Serializable {
    private String token;
    private String refreshToken;
}
