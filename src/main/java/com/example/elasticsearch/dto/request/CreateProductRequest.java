package com.example.elasticsearch.dto.request;

import com.example.elasticsearch.enumerate.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateProductRequest(

        @NotBlank
        String name,

        @NotNull
        BigDecimal price,

        String description,

        @NotNull
        ProductStatus status
) { }
