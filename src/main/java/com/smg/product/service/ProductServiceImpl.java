package com.smg.product.service;

import com.smg.product.dto.ProductCreateDTO;
import com.smg.product.dto.ProductUpdateDTO;
import com.smg.product.exception.NotFoundException;
import com.smg.product.kafka.ProductProducer;
import com.smg.product.mapper.ProductMapper;
import com.smg.product.model.Product;
import com.smg.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final ProductProducer productProducer;

    @Override
    @Transactional
    public Product create(ProductCreateDTO createDTO) {
        var product = mapper.map(createDTO);
        var savedProduct = repository.save(product);
        productProducer.sendProductMessage(mapper.mapToKafka(savedProduct));
        return savedProduct;
    }

    @Override
    public Product getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found."));
    }

    @Override
    public List<Product> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public Product update(Long id, ProductUpdateDTO updateDTO) {
        var product = getById(id);
        mapper.updateProductFromDto(updateDTO, product);
        return repository.save(product);

    }

    @Override
    @Transactional
    public void delete(Long id) {
        getById(id);
        repository.deleteById(id);
    }
}
