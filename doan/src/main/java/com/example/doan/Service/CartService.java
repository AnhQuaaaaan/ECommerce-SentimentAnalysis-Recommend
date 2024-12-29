package com.example.doan.Service;

import com.example.doan.Dto.CartDto;

public interface CartService {
    void addtoCart(CartDto cartDto,String productId);
}
