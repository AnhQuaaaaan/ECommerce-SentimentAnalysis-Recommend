package com.example.doan.Controller;

import com.example.doan.Dto.OrderDto;
import com.example.doan.Dto.ProductDto;
import com.example.doan.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    @Autowired
    private OrderService orderService;
    @GetMapping("/customer/{id}")
    public ResponseEntity<?> getOrderByCustomer(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "12") int size, @PathVariable String id) {
        try {
            Page<OrderDto> orderDtos = orderService.getAllOrderByCustomer(page, size,id);
            return ResponseEntity.ok(orderDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable int id) {
        try {
            OrderDto orderDto = orderService.getOrderById(id);
            if (orderDto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
            }
            return ResponseEntity.ok(orderDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PostMapping
    public ResponseEntity<?> create(@RequestBody OrderDto orderDto) {
        try {
            return ResponseEntity.ok(orderService.create(orderDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
//    @PostMapping("/send-product-data")
//    public ResponseEntity<?> sendProductData(@RequestBody EmailDto request) {
//        try {
//            mailService.sendReview(request);
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(e.getMessage());
//        }
//    }
}
