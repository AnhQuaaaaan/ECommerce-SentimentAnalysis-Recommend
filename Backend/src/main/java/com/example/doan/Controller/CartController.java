package com.example.doan.Controller;

import com.example.doan.Dto.CartDto;
import com.example.doan.Dto.CartItemsDto;
import com.example.doan.Dto.CustomerDto;
import com.example.doan.Dto.OrderDto;
import com.example.doan.Entity.Cart;
import com.example.doan.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    @Autowired
    private CartService cartService;
    @PostMapping("/{productId}")
    public ResponseEntity<?> addToCart(@RequestBody CartDto cartDto, @PathVariable String productId) {
        try {
            cartService.addtoCart(cartDto,productId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
