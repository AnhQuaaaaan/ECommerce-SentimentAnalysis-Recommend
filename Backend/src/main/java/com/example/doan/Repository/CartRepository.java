package com.example.doan.Repository;

import com.example.doan.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Cart findCartByCustomer_Id(String id);
}
