package com.example.elasticsearch.controller;

import com.example.elasticsearch.dto.PageResponseDto;
import com.example.elasticsearch.dto.ProductCriteria;
import com.example.elasticsearch.dto.ProductDto;
import com.example.elasticsearch.dto.request.BulkDeleteProductRequest;
import com.example.elasticsearch.dto.request.CreateProductRequest;
import com.example.elasticsearch.enumerate.ProductStatus;
import com.example.elasticsearch.service.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    @GetMapping
    public PageResponseDto<ProductDto> findAllProduct(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(name = "fromDate", required = false)
            @DateTimeFormat(pattern = "dd-MM-yyyy") Date fromDate,
            @RequestParam(name = "toDate", required = false)
            @DateTimeFormat(pattern = "dd-MM-yyyy") Date toDate,
            @RequestParam(name = "statuses", required = false) List<ProductStatus> statuses) throws IOException {
        Page<ProductDto> products = productService.getAll(
                ProductCriteria.builder()
                        .name(name)
                        .minPrice(minPrice)
                        .maxPrice(maxPrice)
                        .statuses(statuses)
                        .fromDate(fromDate)
                        .toDate(toDate)
                        .build(),
                PageRequest.of(page, size)
        );

        return PageResponseDto.<ProductDto>builder()
                .currentPage(page)
                .pageSize(size)
                .totalCount(products.getTotalElements())
                .totalPages(products.getTotalPages())
                .data(products.getContent())
                .build();
    }

    @GetMapping("/{id}")
    public ProductDto findProductById(@PathVariable("id") String id) {
        return productService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto createProduct(@RequestBody @Valid CreateProductRequest request) {
        return productService.create(request);
    }

    @PutMapping("/{id}")
    public ProductDto updateProduct(@PathVariable("id") String id,
                                    @RequestBody @Valid CreateProductRequest request) {
        return productService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(@PathVariable("id") String id) {
        productService.deleteById(id);
    }

    @DeleteMapping
    public void bulkDeleteProduct(@RequestBody BulkDeleteProductRequest request) {
        productService.bulkDelete(request.ids());
    }

}
