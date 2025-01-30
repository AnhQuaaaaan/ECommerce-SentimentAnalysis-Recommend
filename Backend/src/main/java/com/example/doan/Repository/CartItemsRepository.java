package com.example.doan.Repository;

import com.example.doan.Entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Integer> {
    CartItems findCartItemsByCartIdAndProductcartsId(int cartiId,String productId);
    List<CartItems> findAllByCart_Id(int cartId);
}
