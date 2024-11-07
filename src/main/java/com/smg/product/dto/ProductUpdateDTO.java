package com.smg.product.dto;

import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
public record ProductUpdateDTO(
        String name,
        BigDecimal price
) implements Serializable {
}
