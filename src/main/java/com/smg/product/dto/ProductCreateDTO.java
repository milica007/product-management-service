package com.smg.product.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductCreateDTO(
        @NotNull String name,
        @Max(value = 1000, message = "Price must be less than or equal to 1000") BigDecimal price

) {
}
