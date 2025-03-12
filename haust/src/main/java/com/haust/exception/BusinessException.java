package com.haust.exception;

public class BusinessException extends RuntimeException {
    private final String code; // 错误码
    private final String message; // 错误信息

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}