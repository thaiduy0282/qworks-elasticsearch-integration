package com.example.elasticsearch.dto;

import com.example.elasticsearch.enumerate.ProductStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

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

    private Date createdDate;

    private Date updatedDate;

}
