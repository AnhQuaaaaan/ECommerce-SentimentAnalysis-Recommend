package com.example.doan.Controller;

import com.example.doan.Dto.OrderDto;
import com.example.doan.Dto.OrderItemsDto;
import com.example.doan.Service.OrderItemsService;
import com.example.doan.Service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/orderItems")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderItemsController {
    OrderItemsService orderItemsService;
    @PostMapping
    public ResponseEntity<?> save(@RequestBody OrderItemsDto orderItemsDto) {
        try {
            orderItemsService.save(orderItemsDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/order/{id}")
    public ResponseEntity<?> getOrderItemByOrder(@PathVariable int id) {
        try {
            return ResponseEntity.ok(orderItemsService.getOrderItemByOrder(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PutMapping
    public ResponseEntity<?> updatehasReviewed(@RequestBody OrderItemsDto orderItemsDto) {
        try {
            orderItemsService.update(orderItemsDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
