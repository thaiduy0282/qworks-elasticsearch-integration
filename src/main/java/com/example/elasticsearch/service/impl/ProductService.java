package com.example.elasticsearch.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import com.example.elasticsearch.dto.ProductCriteria;
import com.example.elasticsearch.dto.ProductDto;
import com.example.elasticsearch.dto.request.CreateProductRequest;
import com.example.elasticsearch.entity.ProductEntity;
import com.example.elasticsearch.exception.ResourceNotFoundException;
import com.example.elasticsearch.mapper.ProductMapper;
import com.example.elasticsearch.repository.ProductRepository;
import com.example.elasticsearch.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;

    private final ElasticsearchClient elasticsearchClient;

    @Override
    public Page<ProductDto> getAll(ProductCriteria criteria, Pageable pageable) throws IOException {

        BoolQuery.Builder boolQuery = buildProductQueryBuilder(criteria);

        SearchResponse<ProductEntity> response = elasticsearchClient.search(s -> s
                        .index("product")
                        .query(q -> q.bool(boolQuery.build()))
                        .from((int) pageable.getOffset())
                        .size(pageable.getPageSize()),
                ProductEntity.class);

        List<ProductDto> productDtos = response.hits()
                .hits()
                .stream()
                .map(hit -> {
                    if (StringUtils.hasLength(hit.id()) && hit.source() != null) {
                        return ProductMapper.dtoFromEntity(hit.id(), hit.source());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        long totalHits = response.hits().total() != null ? response.hits().total().value() : 0;

        return new PageImpl<>(productDtos, pageable, totalHits);

    }

    private BoolQuery.Builder buildProductQueryBuilder(ProductCriteria criteria) {
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();

        if (StringUtils.hasLength(criteria.getName())) {
            boolQuery.must(s -> s.match(t -> t.field(ProductEntity.Fields.name).query(criteria.getName())));
        }

        if (criteria.getMinPrice() != null && criteria.getMaxPrice() != null) {
            boolQuery.filter(f ->
                    f.range(r -> r.field(ProductEntity.Fields.price)
                            .gte(JsonData.of(criteria.getMinPrice().doubleValue()))
                            .lte(JsonData.of(criteria.getMaxPrice().doubleValue()))
                    )
            );
        } else if (criteria.getMinPrice() != null) {
            boolQuery.filter(f -> f.range(r -> r.field(ProductEntity.Fields.price)
                    .gte((JsonData) criteria.getMinPrice())));
        } else if (criteria.getMaxPrice() != null) {
            boolQuery.filter(f -> f.range(r -> r.field(ProductEntity.Fields.price)
                    .lte((JsonData) criteria.getMaxPrice())));
        }

        if (criteria.getStatuses() != null && !criteria.getStatuses().isEmpty()) {
            boolQuery.filter(f -> f
                    .terms(t -> t
                            .field(ProductEntity.Fields.status + ".keyword")
                            .terms(v -> v
                                    .value(criteria.getStatuses().stream()
                                            .map(status -> FieldValue.of(status.name()))
                                            .collect(Collectors.toList()))
                            )
                    )
            );
        }

        if (criteria.getFromDate() != null && criteria.getToDate() != null) {
            boolQuery.filter(f -> f
                    .range(r -> r.field(ProductEntity.Fields.createdDate)
                            .gte(JsonData.of(criteria.getFromDate().getTime()))
                            .lte(JsonData.of(criteria.getToDate().getTime()))
                    )
            );
        }

        return boolQuery;
    }


    @Override
    public ProductDto getById(String id) {
        return productRepository.findById(id)
                .map(ProductMapper::dtoFromEntity)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id " + id
                ));
    }

    @Override
    public ProductDto create(CreateProductRequest request) {
        ProductEntity entity = ProductMapper.entityFromRequest(request);
        entity.setCreatedDate(new Date());
        return ProductMapper.dtoFromEntity(productRepository.save(entity));
    }

    @Override
    public ProductDto update(String id, CreateProductRequest request) {
        ProductEntity existedProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id " + id
                ));

        existedProduct.setName(request.name());
        existedProduct.setPrice(request.price());
        existedProduct.setStatus(request.status());
        existedProduct.setDescription(request.description());
        existedProduct.setUpdatedDate(new Date());

        return ProductMapper.dtoFromEntity(productRepository.save(existedProduct));
    }

    @Override
    public void deleteById(String id) {
        productRepository.deleteById(id);
    }

    @Override
    public void bulkDelete(List<String> ids) {
        productRepository.deleteAllById(ids);
    }

}
