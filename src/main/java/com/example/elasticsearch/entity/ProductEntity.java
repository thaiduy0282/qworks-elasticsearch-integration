package com.example.elasticsearch.entity;

import com.example.elasticsearch.enumerate.ProductStatus;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;
import java.util.Date;

@Document(indexName = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldNameConstants
public class ProductEntity {

    @Id
    private String id;

    private String name;

    private BigDecimal price;

    private String description;

    private ProductStatus status;

    @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date updatedDate;

}
