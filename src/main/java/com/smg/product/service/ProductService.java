package com.smg.product.service;

import com.smg.product.dto.ProductCreateDTO;
import com.smg.product.dto.ProductUpdateDTO;
import com.smg.product.model.Product;

import java.util.List;

public interface ProductService {
    Product create(ProductCreateDTO createDTO);
    Product getById(Long id);
    List<Product> getAll();
    Product update(Long id, ProductUpdateDTO updateDTO);
    void delete(Long id);
}
