package com.example.doan.Service.Impl;

import com.example.doan.Dto.CartDto;
import com.example.doan.Dto.CartItemsDto;
import com.example.doan.Entity.Cart;
import com.example.doan.Entity.CartItems;
import com.example.doan.Entity.Product;
import com.example.doan.Repository.CartItemsRepository;
import com.example.doan.Repository.CartRepository;
import com.example.doan.Repository.ProductRepository;
import com.example.doan.Service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService {
    CartRepository cartRepository;
    CartItemsRepository cartItemsRepository;
    ProductRepository productRepository;
    @Override
    public void addtoCart(CartDto cartDto,String productId) {
        Optional<Product> productOptional=productRepository.findById(productId);
        Product product=productOptional.get();
        if (cartRepository.findCartByCustomer_Id(cartDto.getCustomer().getId())==null){
            Cart cart=new Cart();
            cart.setCustomer(cartDto.getCustomer());
            cartRepository.save(cart);
            CartItems cartItems= new CartItems();
            cartItems.setPrice(product.getPrice());
            cartItems.setProductcarts(product);
            cartItems.setQuantity(1);
            cartItems.setCart(cart);
            cartItemsRepository.save(cartItems);
        }
        else{
            Cart cart=cartRepository.findCartByCustomer_Id(cartDto.getCustomer().getId());
            CartItems cartItems=cartItemsRepository.findCartItemsByCartIdAndProductcartsId(cart.getId(),productId);
            if (cartItems!=null){
                cartItems.setQuantity(cartItems.getQuantity()+1);
                cartItemsRepository.save(cartItems);
            }
            else{
                CartItems cartItems1= new CartItems();
                cartItems1.setPrice(product.getPrice());
                cartItems1.setProductcarts(product);
                cartItems1.setQuantity(1);
                cartItems1.setCart(cart);
                cartItemsRepository.save(cartItems1);
            }
        }
    }

}
