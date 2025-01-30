package com.example.doan.Service;

import com.example.doan.Dto.CartItemsDto;
import com.example.doan.Entity.CartItems;

import java.util.List;

public interface CartItemsService {
    List<CartItemsDto> getCartItemsbyCustomer(String customerId);
    void updateCartItemQuantity(CartItemsDto cartItemsDto);
    void removeProductFromCart(int id);
}
