package com.example.doan.Controller;

import com.example.doan.Dto.CustomerDto;
import com.example.doan.Service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CustomerDto customerDto) {
        CustomerDto customerDto1=customerService.login(customerDto);
        if (customerDto1!=null){
            return ResponseEntity.ok(customerDto1);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tên đăng nhập hoặc mật khẩu không đúng");
    }
    @PostMapping("/register")
    public ResponseEntity<?> Register(@RequestBody CustomerDto customerDto){
        if (customerService.register(customerDto)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tài khoản đã tồn tại");
    }
}
