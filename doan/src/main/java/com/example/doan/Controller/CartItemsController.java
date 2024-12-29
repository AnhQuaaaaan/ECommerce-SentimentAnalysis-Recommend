package com.example.doan.Controller;

import com.example.doan.Dto.CartItemsDto;
import com.example.doan.Dto.OrderDto;
import com.example.doan.Service.CartItemsService;
import com.example.doan.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/cartItems")
@RequiredArgsConstructor
public class CartItemsController {
    @Autowired
    private CartItemsService cartItemsService;
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getCartItemsbyCustomer(@PathVariable String customerId) {
        try {
            List<CartItemsDto> cartItemsDtos=cartItemsService.getCartItemsbyCustomer(customerId);
            return ResponseEntity.ok(cartItemsDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PutMapping
    public ResponseEntity<?> updateCartItemQuantity(@RequestBody CartItemsDto cartItemsDto) {
        try {
            cartItemsService.updateCartItemQuantity(cartItemsDto);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeProductFromCart(@PathVariable int id) {

        try {
            cartItemsService.removeProductFromCart(id);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
