package com.example.doan.Service;

import com.example.doan.Dto.CustomerDto;
import com.example.doan.Entity.Customer;

public interface CustomerService {
    boolean register(CustomerDto customerDto);
    CustomerDto login(CustomerDto customerDto);
}
