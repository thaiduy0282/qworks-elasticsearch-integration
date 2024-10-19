package com.example.elasticsearch.repository;

import com.example.elasticsearch.entity.ProductEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ElasticsearchRepository<ProductEntity, String> {
}
