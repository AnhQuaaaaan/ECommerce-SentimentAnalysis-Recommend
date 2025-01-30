package com.example.doan.Service;

import com.example.doan.Dto.OrderItemsDto;
import com.example.doan.Entity.Order;
import com.example.doan.Entity.OrderItems;

import java.util.List;

public interface OrderItemsService {
    void save(OrderItemsDto orderItemsDto);
    List<OrderItemsDto> getOrderItemByOrder(int id);
    void update(OrderItemsDto orderItemsDto);
}
