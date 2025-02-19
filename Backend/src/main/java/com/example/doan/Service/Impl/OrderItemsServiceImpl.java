package com.example.doan.Service.Impl;

import com.example.doan.Dto.OrderItemsDto;
import com.example.doan.Entity.Cart;
import com.example.doan.Entity.CartItems;
import com.example.doan.Entity.OrderItems;
import com.example.doan.Repository.CartItemsRepository;
import com.example.doan.Repository.CartRepository;
import com.example.doan.Repository.OrderItemsRepository;
import com.example.doan.Service.OrderItemsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderItemsServiceImpl implements OrderItemsService {
    OrderItemsRepository orderItemsRepository;
    @Override
    public void save(OrderItemsDto orderItemsDto) {
        orderItemsRepository.save(convertToOrderItems(orderItemsDto));
    }

    @Override
    public List<OrderItemsDto> getOrderItemByOrder(int id) {
        List<OrderItems>orderItems=orderItemsRepository.findAllByOrderId(id);
        List<OrderItemsDto> orderItemsDtos=orderItems.stream().map(this::convertToOrderItemsDto).toList();
        return orderItemsDtos;
    }

    @Override
    public void update(OrderItemsDto orderItemsDto) {
        Optional<OrderItems> optionalOrderItems= orderItemsRepository.findById(orderItemsDto.getId());
        OrderItems orderItems=optionalOrderItems.get();
        orderItems.setHasReviewed(orderItemsDto.isHasReviewed());
        orderItemsRepository.save(orderItems);
    }

    public OrderItems convertToOrderItems(OrderItemsDto orderItemsDto){
        OrderItems orderItems=new OrderItems();
        orderItems.setProductitems(orderItemsDto.getProduct());
        orderItems.setQuantity(orderItemsDto.getQuantity());
        orderItems.setOrder(orderItemsDto.getOrder());
        orderItems.setHasReviewed(orderItemsDto.isHasReviewed());
        orderItems.setPrice(orderItemsDto.getPrice());
        return orderItems;
    }
    public OrderItemsDto convertToOrderItemsDto(OrderItems orderItems){
        OrderItemsDto orderItemsDto=new OrderItemsDto();
        orderItemsDto.setProduct(orderItems.getProductitems());
        orderItemsDto.setHasReviewed(orderItems.isHasReviewed());
        orderItemsDto.setQuantity(orderItems.getQuantity());
        orderItemsDto.setId(orderItems.getId());
        orderItemsDto.setPrice(orderItems.getPrice());
        return orderItemsDto;
    }
}
