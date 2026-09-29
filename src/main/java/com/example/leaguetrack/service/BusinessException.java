package com.example.leaguetrack.service;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {
    private final HttpStatus status;

    public BusinessException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() { return status; }

    public static BusinessException notFound(String m)   { return new BusinessException(HttpStatus.NOT_FOUND, m); }
    public static BusinessException badRequest(String m) { return new BusinessException(HttpStatus.BAD_REQUEST, m); }
    public static BusinessException conflict(String m)   { return new BusinessException(HttpStatus.CONFLICT, m); }
}