package me.holiday.common.exception;

import org.springframework.http.HttpStatus;

public class ServerException extends DefaultException {
    public ServerException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER ERROR", null);
    }
}
