package me.holiday.auth.common.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Builder
public record ApiErrorResponse(
        HttpStatusCode code,
        HttpStatus status,
        String message,
        LocalDateTime timestamp
) {

    public static ApiErrorResponse of(DefaultException e) {
        return ApiErrorResponse.builder()
                .message(e.message)
                .code(e.httpStatus)
                .status(e.httpStatus)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
