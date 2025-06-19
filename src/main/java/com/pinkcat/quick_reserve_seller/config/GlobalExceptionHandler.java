package com.pinkcat.quick_reserve_seller.config;

import com.pinkcat.quick_reserve_seller.common.exceptions.PinkCatException;
import com.pinkcat.quick_reserve_seller.common.model.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(PinkCatException.class)
    public ResponseEntity<BaseResponse<String>> handleKeonException(PinkCatException ex, WebRequest request) {
        BaseResponse<String> response = new BaseResponse<>(ex); // 커스텀 응답 객체 생성
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}