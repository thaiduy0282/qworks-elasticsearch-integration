package com.example.elasticsearch.dto;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class PageResponseDto<T> {

    Integer currentPage;

    Integer pageSize;

    Long totalCount;

    Integer totalPages;

    List<T> data;

}
