package me.holiday.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DefaultException.class)
    public ResponseEntity<ApiErrorResponse> globalException(DefaultException e) {
        log.error("[Error] : {} - {} - {}",
                e.httpStatus,
                e.message,
                e.data);

        return ResponseEntity.status(e.httpStatus)
                .body(ApiErrorResponse.of(e));
    }
}
