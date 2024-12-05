package me.holiday.auth.exception;

import me.holiday.common.exception.DefaultException;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class MemberException extends DefaultException {
    public MemberException(HttpStatus status, String message, Map<String, Object> data) {
        super(status, message, data);
    }
}
