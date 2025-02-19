package com.example.doan.Service.Impl;

import com.example.doan.Dto.OrderDto;
import com.example.doan.Dto.ProductDto;
import com.example.doan.Entity.*;
import com.example.doan.Repository.CartItemsRepository;
import com.example.doan.Repository.CartRepository;
import com.example.doan.Repository.OrderItemsRepository;
import com.example.doan.Repository.OrderRepository;
import com.example.doan.Service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;
    CartItemsRepository cartItemsRepository;
    CartRepository cartRepository;
    OrderItemsRepository orderItemsRepository;
    @Override
    public OrderDto create(OrderDto orderDto) {
        Order order=convertToOrder(orderDto);
        orderRepository.save(order);
        Cart cart= cartRepository.findCartByCustomer_Id(orderDto.getCustomer().getId());
        List<CartItems> cartItemsList = cartItemsRepository.findAllByCart_Id(cart.getId());
        for (CartItems x: cartItemsList){
            OrderItems orderItems = new OrderItems();
            orderItems.setOrder(order);
            orderItems.setProductitems(x.getProductcarts());
            orderItems.setPrice(x.getPrice());
            orderItems.setQuantity(x.getQuantity());
            orderItems.setHasReviewed(false);
            orderItemsRepository.save(orderItems);
            cartItemsRepository.delete(x);
        }
        return convertToOrderDto(order);
    }

    @Override
    public Page<OrderDto> getAllOrderByCustomer(int page, int size,String customerId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orderPage;
        orderPage = orderRepository.findAllByCustomerorderId(pageable,customerId);
        Page<OrderDto> orderDtos = orderPage.map(this::convertToOrderDto);
        return orderDtos;
    }
    @Override
    public OrderDto getOrderById(int id) {
        try {
            Optional<Order> orderOptional=orderRepository.findById(id);
            if (orderOptional.isPresent()) {
                Order order = orderOptional.get();
                return convertToOrderDto(order);
            } else {
                throw new RuntimeException("Order not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public Order convertToOrder(OrderDto orderDto){
        Order order=new Order();
        order.setCustomerorder(orderDto.getCustomer());
        order.setNote(orderDto.getNote());
        order.setCustomerName(orderDto.getCustomerName());
        order.setCustomerPhone(orderDto.getCustomerPhone());
        order.setEmail(orderDto.getEmail());
        order.setDeliveryAddress(orderDto.getDeliveryAddress());
        order.setTotalAmount(orderDto.getTotalAmount());
        order.setOrderDate(orderDto.getOrderDate());
        return order;
    }
    public OrderDto convertToOrderDto(Order order){
        OrderDto orderDto=new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setCustomer(order.getCustomerorder());
        orderDto.setNote(order.getNote());
        orderDto.setCustomerName(order.getCustomerName());
        orderDto.setCustomerPhone(order.getCustomerPhone());
        orderDto.setEmail(order.getEmail());
        orderDto.setDeliveryAddress(order.getDeliveryAddress());
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setOrderDate(order.getOrderDate());
        return orderDto;
    }
}
