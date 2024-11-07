package com.smg.product.dto.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "error")
public class ErrorDTO implements Serializable {

    @JsonProperty("error_code")
    private ErrorCode code;

    @JsonProperty("error_description")
    private String description;
}
