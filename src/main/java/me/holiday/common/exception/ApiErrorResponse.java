package me.holiday.common.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
public record ApiErrorResponse(
        Integer code,
        HttpStatus status,
        String message,
        LocalDateTime timestamp
) {

    public static ApiErrorResponse of(DefaultException e) {
        return ApiErrorResponse.builder()
                .message(e.message)
                .code(e.httpStatus.value())
                .status(e.httpStatus)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
