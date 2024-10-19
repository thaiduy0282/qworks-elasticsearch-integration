package com.example.elasticsearch.service;

import com.example.elasticsearch.dto.ProductCriteria;
import com.example.elasticsearch.dto.ProductDto;
import com.example.elasticsearch.dto.request.CreateProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

/**
 * The interface Product service.
 */
public interface IProductService {

    /**
     * Gets all.
     *
     * @param criteria the criteria
     * @param pageable the pageable
     * @return the all
     * @throws IOException the io exception
     */
    Page<ProductDto> getAll(ProductCriteria criteria, Pageable pageable) throws IOException;

    /**
     * Gets by id.
     *
     * @param id the id
     * @return the by id
     */
    ProductDto getById(String id);

    /**
     * Create product dto.
     *
     * @param productDto the product dto
     * @return the product dto
     */
    ProductDto create(CreateProductRequest productDto);

    /**
     * Update product dto.
     *
     * @param id      the id
     * @param request the request
     * @return the product dto
     */
    ProductDto update(String id, CreateProductRequest request);

    /**
     * Delete by id.
     *
     * @param id the id
     */
    void deleteById(String id);

    /**
     * Bulk delete.
     *
     * @param ids the ids
     */
    void bulkDelete(List<String> ids);

}
