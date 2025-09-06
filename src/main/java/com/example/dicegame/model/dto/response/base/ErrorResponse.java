package com.example.dicegame.model.dto.response.base;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ErrorResponse {
    // Status/identification information
    private String status;
    private String errorKey;

    // Primary error information
    private String message;

    // Context information
    private String path;
    private String entityName;

    // Detailed/nested information
    private Map<String, String> errors;
    private MetaData meta;
}