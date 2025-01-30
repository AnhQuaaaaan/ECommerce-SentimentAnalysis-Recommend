package com.example.doan.Repository;

import com.example.doan.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository  extends JpaRepository<Customer, String> {
    Customer findCustomerByUsernameAndPassword(String username,String password);
}
