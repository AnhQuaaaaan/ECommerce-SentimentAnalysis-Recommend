package com.example.doan.Service.Impl;

import com.example.doan.Dto.CartItemsDto;
import com.example.doan.Entity.Cart;
import com.example.doan.Entity.CartItems;
import com.example.doan.Repository.CartItemsRepository;
import com.example.doan.Repository.CartRepository;
import com.example.doan.Service.CartItemsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartItemsServiceImpl implements CartItemsService {
    CartItemsRepository cartItemsRepository;
    CartRepository cartRepository;
    @Override
    public List<CartItemsDto> getCartItemsbyCustomer(String customerId) {
        Cart cart= cartRepository.findCartByCustomer_Id(customerId);
        List<CartItems> cartItemsList = cartItemsRepository.findAllByCart_Id(cart.getId());
        return cartItemsList.stream()
                .map(this::converToCartItemsDto)
                .collect(Collectors.toList());
    }


    public void updateCartItemQuantity(CartItemsDto cartItemsDto) {
        CartItems cartItem = cartItemsRepository.findById(cartItemsDto.getId())
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cartItem.setQuantity(cartItemsDto.getQuantity());
        cartItemsRepository.save(cartItem);
    }

    public void removeProductFromCart(int id) {
        cartItemsRepository.deleteById(id);
    }


    public  CartItemsDto converToCartItemsDto(CartItems cartItems) {
        CartItemsDto dto = new CartItemsDto();
        dto.setId(cartItems.getId());
        dto.setQuantity(cartItems.getQuantity());
        dto.setPrice(cartItems.getPrice());
        dto.setCart(cartItems.getCart());
        dto.setProduct(cartItems.getProductcarts());
        return dto;
    }
}
