package com.pinkcat.quick_reserve_seller.config;

import com.pinkcat.quick_reserve_seller.common.exceptions.PinkCatException;
import com.pinkcat.quick_reserve_seller.common.model.BaseResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.LinkedHashMap;

@RestControllerAdvice
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return !returnType.getParameterType().equals(BaseResponse.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof LinkedHashMap<?, ?> && ((LinkedHashMap<String, ?>) body).containsKey("error")) return body;

        if (body instanceof BaseResponse) return body;

        return new BaseResponse<>(body);
    }

    @ExceptionHandler(PinkCatException.class)
    public ResponseEntity<BaseResponse<String>> handlePinkCatException(PinkCatException ex, WebRequest request) {
        BaseResponse<String> response = new BaseResponse<>(ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}