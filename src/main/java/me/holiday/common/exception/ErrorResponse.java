package me.holiday.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

public interface ErrorResponse {
    HttpStatusCode code();
    HttpStatus status();
    String message();
    LocalDateTime timeStamp();
}
