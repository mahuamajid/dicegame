package com.example.dicegame.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Globar error status codes and messages.
 */

@Getter
@RequiredArgsConstructor
public enum GlobalErrorStatusDictionary {

    //Success code and messages

    // Error code & messages
    VALIDATION_ERROR("999-000-0000-1", "Validation error"),
    INTERNAL_SERVER_ERROR("999-000-0001-1", "Unexpected error for data processing."),
    CONSTRAINT_VIOLATION_ERROR("999-000-0002-1", "Constraint Violation error"),
    DATA_INTEGRITY_ERROR("999-000-0003-1", "Data Integrity Error");


    private final String statusCode;
    private final String message;

}
