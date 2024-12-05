package me.holiday.common.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class AuthException extends DefaultException {
    public AuthException(HttpStatus httpStatus, String message, Map<String, Object> data) {
        super(httpStatus, message, data);
    }
}
