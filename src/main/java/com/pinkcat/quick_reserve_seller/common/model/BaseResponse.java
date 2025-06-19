package com.pinkcat.quick_reserve_seller.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pinkcat.quick_reserve_seller.common.exceptions.PinkCatException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
    long timestamp = Instant.now().toEpochMilli();
    int status = 200;
    String error = "";
    String message = "";
    String exception = "";
    T data = null;

    public BaseResponse(String message, PinkCatException customException ) {
        customException.printStackTrace();
        this.message = message;
        this.status = customException.getErrorMessageCode().getCode();
        this.error = customException.getErrors().toString();
        this.exception = customException.getErrorMessageCode().name();
    }

    public BaseResponse(PinkCatException customException ) {
        customException.printStackTrace();
        this.message = customException.getMessage();
        this.status = customException.getErrorMessageCode().getCode();
        this.error = customException.getErrors().toString();
        this.exception = customException.getErrorMessageCode().name();
    }

    public BaseResponse(T data) {
        this.data = data;
    }
}