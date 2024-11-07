package com.smg.product.controller;

import com.smg.product.controller.definition.ProductApi;
import com.smg.product.dto.ProductCreateDTO;
import com.smg.product.dto.ProductDTO;
import com.smg.product.dto.ProductUpdateDTO;
import com.smg.product.mapper.ProductMapper;
import com.smg.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
public class ProductController implements ProductApi {

    private final ProductService service;
    private final ProductMapper mapper;

    @Override
    public ProductDTO create(ProductCreateDTO createDTO) {
        return mapper.map(service.create(createDTO));
    }

    @Override
    public ProductDTO getById(Long id) {
        return mapper.map(service.getById(id));
    }

    @Override
    public List<ProductDTO> getAll() {
        return mapper.map(service.getAll());
    }

    @Override
    public ProductDTO update(Long id, ProductUpdateDTO updateDTO) {
        return mapper.map(service.update(id, updateDTO));
    }

    @Override
    public void delete(Long id) {
        service.delete(id);
    }
}
