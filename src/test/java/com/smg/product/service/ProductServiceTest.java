package com.smg.product.service;

import com.smg.product.dto.ProductCreateDTO;
import com.smg.product.dto.ProductKafkaDTO;
import com.smg.product.dto.ProductUpdateDTO;
import com.smg.product.exception.NotFoundException;
import com.smg.product.kafka.ProductProducer;
import com.smg.product.mapper.ProductMapper;
import com.smg.product.model.Product;
import com.smg.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductMapper mapper;

    @Mock
    private ProductProducer productProducer;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateProductAndSendKafkaMessage() {
        var createDTO = createProductCreateDTO().build();
        var product = createProduct().build();
        var productKafkaDTO = createProductKafkaDTO().build();

        when(mapper.map(createDTO)).thenReturn(product);
        when(repository.save(product)).thenReturn(product);
        when(mapper.mapToKafka(product)).thenReturn(productKafkaDTO);

        var result = productService.create(createDTO);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        verify(repository, times(1)).save(product);
        verify(productProducer, times(1)).sendProductMessage(productKafkaDTO);
    }

    @Test
    void shouldGetProductById() {
        var id = 1L;
        var product = createProduct().build();

        when(repository.findById(id)).thenReturn(Optional.of(product));

        var result = productService.getById(id);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        verify(repository, times(1)).findById(id);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenProductNotFound() {
        var id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.getById(id));
        verify(repository, times(1)).findById(id);
    }

    @Test
    void shouldUpdateProduct() {
        var id = 1L;
        var updateDTO = createProductUpdateDTO().build();
        var product = createProduct().build();

        when(repository.findById(id)).thenReturn(Optional.of(product));
        when(repository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        product.setName(updateDTO.name());
        product.setPrice(updateDTO.price());

        var result = productService.update(id, updateDTO);

        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        assertEquals(BigDecimal.valueOf(150.00), result.getPrice());
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(product);
    }

    @Test
    void shouldDeleteProduct() {
        var id = 1L;
        var product = createProduct().build();

        when(repository.findById(id)).thenReturn(Optional.of(product));

        productService.delete(id);

        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).deleteById(id);
    }

    private ProductCreateDTO.ProductCreateDTOBuilder createProductCreateDTO() {
        return ProductCreateDTO.builder()
                .name("Test Product")
                .price(BigDecimal.valueOf(100.00));
    }

    private ProductUpdateDTO.ProductUpdateDTOBuilder createProductUpdateDTO() {
        return ProductUpdateDTO.builder()
                .name("Updated Product")
                .price(BigDecimal.valueOf(150.00));
    }

    private Product.ProductBuilder createProduct() {
        return Product.builder()
                .id(1L)
                .name("Test Product")
                .price(BigDecimal.valueOf(100.00));
    }

    private ProductKafkaDTO.ProductKafkaDTOBuilder createProductKafkaDTO() {
        return ProductKafkaDTO.builder()
                .id(1L)
                .name("Test Product")
                .price(BigDecimal.valueOf(100.00));
    }
}
