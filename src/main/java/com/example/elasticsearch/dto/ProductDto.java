package com.example.elasticsearch.dto;

import com.example.elasticsearch.enumerate.ProductStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class ProductDto {

    private String id;

    private String name;

    private BigDecimal price;

    private String description;

    private ProductStatus status;

}
