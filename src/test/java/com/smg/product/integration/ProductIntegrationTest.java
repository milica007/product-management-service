package com.smg.product.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smg.product.config.ApplicationTest;
import com.smg.product.dto.ProductCreateDTO;
import com.smg.product.dto.ProductDTO;
import com.smg.product.dto.ProductUpdateDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ApplicationTest
class ProductIntegrationTest {

    private static final String BASE_URL = "/products";
    private static final String INVALID_ID = "invalidId";
    private static final String VALID_ID = "1";
    private static final String NON_EXISTENT_ID = "999";
    private static final String CLEANUP_SCRIPT = "/db/scripts/cleanup.sql";
    private static final String DATA_INIT_SCRIPT = "/db/scripts/data-init.sql";
    private static final String BAD_REQUEST = "BAD_REQUEST";
    private static final String NOT_FOUND = "NOT_FOUND";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql(scripts = CLEANUP_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = CLEANUP_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldCreateProduct() throws Exception {
        var createDTO = ProductCreateDTO.builder()
                .name("Test Product")
                .price(BigDecimal.valueOf(100.00))
                .build();
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.price").value(100.00));
    }

    @Test
    @Sql(scripts = CLEANUP_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DATA_INIT_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = CLEANUP_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldGetProductById() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + VALID_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Long.parseLong(VALID_ID)))
                .andExpect(jsonPath("$.name").isNotEmpty());
    }

    @Test
    @Sql(scripts = CLEANUP_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DATA_INIT_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = CLEANUP_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldUpdateProduct() throws Exception {
        var updateDTO = ProductUpdateDTO.builder()
                .name("Updated product")
                .price(BigDecimal.valueOf(150.00))
                .build();
        var result = mockMvc.perform(patch(BASE_URL + "/" + VALID_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk());
        var response = result.andReturn().getResponse().getContentAsString();
        ProductDTO updatedProduct = objectMapper.readValue(response, ProductDTO.class);
        Assertions.assertEquals("Updated product", updatedProduct.name());
        Assertions.assertEquals(BigDecimal.valueOf(150.00), updatedProduct.price());
    }

    @Test
    @Sql(scripts = CLEANUP_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DATA_INIT_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = CLEANUP_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldDeleteProduct() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + VALID_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnBadRequestWhenInvalidIdFormat() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + INVALID_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_code").value(BAD_REQUEST))
                .andExpect(jsonPath("$.error_description", containsString("Failed to convert")));
    }

    @Test
    void shouldReturnBadRequestWhenInvalidProductData() throws Exception {
        var createDTO = ProductCreateDTO.builder()
                .name("Test Product")
                .price(BigDecimal.valueOf(1100))
                .build();

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_code").value(BAD_REQUEST))
                .andExpect(jsonPath("$.error_description", containsString("price")));
    }

    @Test
    void shouldReturnBadRequestWhenMalformedJson() throws Exception {
        String malformedJson = "{ \"name\": \"Test Product\", \"price\": }";

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_code").value(BAD_REQUEST))
                .andExpect(jsonPath("$.error_description", containsString("JSON parse error")));
    }

    @Test
    void shouldHandleCustomApiException() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + NON_EXISTENT_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error_code").value(NOT_FOUND))
                .andExpect(jsonPath("$.error_description", containsString("Product not found")));
    }
}
