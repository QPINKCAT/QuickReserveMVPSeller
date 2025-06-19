package com.pinkcat.quick_reserve_seller.common.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PinkCatException extends RuntimeException {

    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    private ErrorMessageCode errorMessageCode = ErrorMessageCode.ERROR;
    private List<PinkCatError> errors = new ArrayList<>();

    public PinkCatException(String message) {
        super(message);
    }

    public PinkCatException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public PinkCatException(String message, HttpStatus httpStatus, ErrorMessageCode errorMessageCode) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorMessageCode = errorMessageCode;
    }

    public PinkCatException(String message, ErrorMessageCode errorMessageCode) {
        super(message);
        this.errorMessageCode = errorMessageCode;
    }

    public PinkCatException(ErrorMessageCode errorMessageCode) {
        this.errorMessageCode = errorMessageCode;
    }

    public HttpStatus getPinkCatHttpStatus() {
        return this.httpStatus;
    }

    public ErrorMessageCode getPinkCatErrorMessageCode() {
        return this.errorMessageCode;
    }

    public List<PinkCatError> getPinkCatErrors() {
        return this.errors;
    }

    public void setPinkCatErrors(List<PinkCatError> errors) {
        this.errors = errors;
    }
}