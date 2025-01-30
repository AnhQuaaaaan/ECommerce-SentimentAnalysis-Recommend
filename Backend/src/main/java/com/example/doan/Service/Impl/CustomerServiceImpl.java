package com.example.doan.Service.Impl;

import com.example.doan.Dto.CustomerDto;
import com.example.doan.Entity.Customer;
import com.example.doan.Repository.CustomerRepository;
import com.example.doan.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Override
    public boolean register(CustomerDto customerDto) {
        if (customerRepository.findCustomerByUsernameAndPassword(customerDto.getUsername(), customerDto.getPassword())!=null){
            return false;
        }
        Customer customer=convertToCustomer(customerDto);
        customerRepository.save(customer);
        return true;
    }

    public CustomerDto login(CustomerDto customerDto) {
        Customer customer=customerRepository.findCustomerByUsernameAndPassword(customerDto.getUsername(), customerDto.getPassword());
        if (customer!=null){
            return convertToCustomerDto(customer);
        }
        return null;
    }
    public CustomerDto convertToCustomerDto(Customer customer){
        CustomerDto customerDto=new CustomerDto();
        customerDto.setId(customer.getId());
        customerDto.setDob(customer.getDob());
        customerDto.setPhone(customer.getPhone());
        customerDto.setAddress(customer.getAddress());
        customerDto.setEmail(customer.getEmail());
        customerDto.setFullname(customer.getFullname());
        customerDto.setUsername(customer.getUsername());
        return customerDto;
    }
    public Customer convertToCustomer(CustomerDto customerDto){
        Customer customer=new Customer();
        customer.setId(generateCustomerId());
        customer.setDob(customerDto.getDob());
        customer.setEmail(customerDto.getEmail());
        customer.setPhone(customerDto.getPhone());
        customer.setAddress(customerDto.getAddress());
        customer.setFullname(customerDto.getFullname());
        customer.setUsername(customerDto.getUsername());
        customer.setPassword(customerDto.getPassword());
        return customer;
    }
    private String generateCustomerId() {
        StringBuilder builder = new StringBuilder("A");
        Random random = new Random();
        String alphaNumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        for (int i = 1; i < 14; i++) {
            int index = random.nextInt(alphaNumeric.length());
            builder.append(alphaNumeric.charAt(index));
        }

        return builder.toString();
    }
}
