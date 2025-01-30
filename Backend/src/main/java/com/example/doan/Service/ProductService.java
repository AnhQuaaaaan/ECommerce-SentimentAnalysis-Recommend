package com.example.doan.Service;

import com.example.doan.Dto.ProductDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    Page<ProductDto> getAllProducts(int page, int size);
    ProductDto getProductById(String id);
    Page<ProductDto> getProductByName(String name,int page,int size);

    List<ProductDto> recommend(String id);
}
