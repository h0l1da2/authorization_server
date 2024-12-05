package me.holiday.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
public class DefaultException extends RuntimeException {
    protected HttpStatus httpStatus;
    protected String message;
    protected Map<String, Object> data;

    public DefaultException(HttpStatus status, String message, Map<String, Object> data) {
        super(message);
        this.message = message;
        this.httpStatus = status;
        this.data = data;
    }

}
