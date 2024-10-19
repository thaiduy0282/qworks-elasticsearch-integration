package com.example.elasticsearch.mapper;

import com.example.elasticsearch.dto.ProductDto;
import com.example.elasticsearch.dto.request.CreateProductRequest;
import com.example.elasticsearch.entity.ProductEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductMapper {

    public static ProductDto dtoFromEntity(ProductEntity entity) {
        return ProductDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .build();
    }

    public static ProductDto dtoFromEntity(String id, ProductEntity entity) {
        return ProductDto.builder()
                .id(id)
                .name(entity.getName())
                .price(entity.getPrice())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .build();
    }

    public static ProductEntity entityFromRequest(CreateProductRequest request) {
        return ProductEntity.builder()
                .name(request.name())
                .price(request.price())
                .description(request.description())
                .status(request.status())
                .build();
    }

}
