package com.pinkcat.quick_reserve_seller.common.exceptions;

public enum ErrorMessageCode {

    SUCCESS(20000, "success"),
    ERROR(50000, "error");

    private final int codeValue;
    private final String message;

    ErrorMessageCode(int codeValue, String message) {
        this.codeValue = codeValue;
        this.message = message;
    }

    public int getCode() {
        return codeValue;
    }

    public String printMessage() {
        return message;
    }
}