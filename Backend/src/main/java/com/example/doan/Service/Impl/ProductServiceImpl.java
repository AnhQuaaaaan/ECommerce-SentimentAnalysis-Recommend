package com.example.doan.Service.Impl;

import com.example.doan.Dto.ProductDto;
import com.example.doan.Entity.Product;
import com.example.doan.Repository.ProductRepository;
import com.example.doan.Service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    @Override
    public Page<ProductDto> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage;
        productPage = productRepository.findAll(pageable);
        Page<ProductDto> productDtos = productPage.map(this::convertToProductDTO);
        return productDtos;
    }

    @Override
    public ProductDto getProductById(String id) {
        try {
            Optional<Product> productOptional=productRepository.findById(id);
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                return convertToProductDTO(product);
            } else {
                throw new RuntimeException("Product not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Page<ProductDto> getProductByName(String name,int page,int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage;
        productPage = productRepository.findProductByNameContaining(name,pageable);
        Page<ProductDto> productDtos = productPage.map(this::convertToProductDTO);
        return productDtos;
    }

    @Override
    public List<ProductDto> recommend(String id) {
        String url = "http://localhost:5000/recommendations?user_id=" + id;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            List<String> listproduct=(List<String>) response.getBody();
            List<ProductDto>productDtos=new ArrayList<>();
            for (String x:listproduct){
                Optional<Product> productOptional=productRepository.findById(x);
                Product product=productOptional.get();
                productDtos.add(convertToProductDTO(product));
            }
            return productDtos;
        } else {
            throw new RuntimeException("Failed to fetch recommendations from Python API");
        }
    }

    private ProductDto convertToProductDTO(Product product) {
        ProductDto productDto=new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setDetails(product.getDetails());
        productDto.setImage(product.getImage());
        productDto.setPrice(product.getPrice());
        return productDto;
    }
}
