package com.example.doan.Repository;

import com.example.doan.Dto.ProductDto;
import com.example.doan.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository  extends JpaRepository<Product, String> {
    Page<Product> findAll(Pageable pageable);
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name%")
    Page<Product> findProductByNameContaining(@Param("name") String name, Pageable pageable);

}
