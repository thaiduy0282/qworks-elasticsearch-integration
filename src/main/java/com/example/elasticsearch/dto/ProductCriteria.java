package com.example.elasticsearch.dto;

import com.example.elasticsearch.enumerate.ProductStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class ProductCriteria {

    private String name;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private List<ProductStatus> statuses;

    private Date fromDate;

    private Date toDate;

}
