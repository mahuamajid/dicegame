package com.example.dicegame.model.dto.response.base;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
public class PaginatedResponse<T> {
    private List<T> content;
    private Pagination pagination;

    public static <T> PaginatedResponse<T> from(Page<T> page) {
        return PaginatedResponse.<T>builder()
            .content(page.getContent())
            .pagination(Pagination.from(page))
            .build();
    }
}
