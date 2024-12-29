package com.example.doan.Service;

import com.example.doan.Dto.OrderDto;
import com.example.doan.Dto.ProductDto;
import org.springframework.data.domain.Page;

public interface OrderService {
    OrderDto create(OrderDto orderDto);
    Page<OrderDto> getAllOrderByCustomer(int page, int size,String customerId);
    OrderDto getOrderById(int id);
}
