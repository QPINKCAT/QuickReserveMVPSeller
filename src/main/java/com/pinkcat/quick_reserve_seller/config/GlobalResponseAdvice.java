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
        System.out.println("]-----] Advice [-----[");
        if (body instanceof ResponseEntity<?> responseEntity) {
            Object inner = responseEntity.getBody();

            if (inner instanceof BaseResponse) return responseEntity;

            BaseResponse<Object> wrapped = new BaseResponse<>(inner);
            return ResponseEntity
                    .status(responseEntity.getStatusCode())
                    .headers(responseEntity.getHeaders())
                    .contentType(selectedContentType)
                    .body(wrapped);
        }

        System.out.println("]-----] Check Response Entity [-----[");

        if (body instanceof LinkedHashMap<?, ?> && ((LinkedHashMap<String, ?>) body).containsKey("error")) return body;

        System.out.println("]-----] Check Response Error [-----[");

        if (body instanceof BaseResponse) return body;

        System.out.println("]-----] Check Response [-----[");

        return new BaseResponse<>(body);
    }

    @ExceptionHandler(PinkCatException.class)
    public ResponseEntity<BaseResponse<String>> handlePinkCatException(PinkCatException ex, WebRequest request) {
        BaseResponse<String> response = new BaseResponse<>(ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}