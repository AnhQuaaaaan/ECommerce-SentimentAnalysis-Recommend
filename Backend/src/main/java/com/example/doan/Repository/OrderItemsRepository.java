package com.example.doan.Repository;

import com.example.doan.Entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemsRepository  extends JpaRepository<OrderItems, Integer> {
    List<OrderItems> findAllByOrderId(int id);
}
