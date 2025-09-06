package com.example.dicegame.model.dto.response.base;

import com.example.dicegame.util.DateTimeUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiResponseFactory {

    public <T> ResponseEntity<ApiResponse<T>> create(T data, String statusCode, String message) {
        return new ResponseEntity<>(ApiResponse.<T>builder()
                .status(statusCode)
                .message(message)
                .data(data)
                .meta(MetaData.builder()
                        .timestamp(DateTimeUtils.getIsoTimestamp())
                        .build())
                .build(), new HttpHeaders(), HttpStatus.CREATED);
    }

    public <T> ResponseEntity<ApiResponse<T>> success(T data, String statusCode, String message) {
        return new ResponseEntity<>(ApiResponse.<T>builder()
                .status(statusCode)
                .message(message)
                .data(data)
                .meta(MetaData.builder()
                        .timestamp(DateTimeUtils.getIsoTimestamp())
                        .build())
                .build(), new HttpHeaders(), HttpStatus.OK);
    }

    public static <T> PaginatedResponse<T> buildPaginatedResponse(
            int page, int size, Page<?> pageInfo, List<T> content) {

        Pagination pagination = Pagination.builder()
                .pageNumber(page)
                .pageSize(size)
                .totalElements(pageInfo.getTotalElements())
                .totalPages(pageInfo.getTotalPages())
                .first(pageInfo.isFirst())
                .last(pageInfo.isLast())
                .build();

        return PaginatedResponse.<T>builder()
                .content(content)
                .pagination(pagination)
                .build();
    }
}
