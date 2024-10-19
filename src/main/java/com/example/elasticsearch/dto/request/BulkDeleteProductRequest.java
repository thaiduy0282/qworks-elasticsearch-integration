package com.example.elasticsearch.dto.request;

import java.util.List;

public record BulkDeleteProductRequest(
        List<String> ids
) { }
