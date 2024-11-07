package com.smg.product.exception;

import com.smg.product.dto.error.ErrorCode;
import com.smg.product.dto.error.ErrorDTO;

public class NotFoundException extends BaseApiException {

    public NotFoundException(String description) {
        super(new ErrorDTO(ErrorCode.NOT_FOUND, description));
    }
}
