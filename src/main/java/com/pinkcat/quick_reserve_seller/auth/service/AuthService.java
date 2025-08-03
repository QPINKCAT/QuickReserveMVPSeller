package com.pinkcat.quick_reserve_seller.auth.service;


import com.pinkcat.quick_reserve_seller.auth.dto.LoginRequestDto;
import com.pinkcat.quick_reserve_seller.auth.dto.LoginResponseDto;
import com.pinkcat.quick_reserve_seller.auth.dto.RefreshTokenResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    LoginResponseDto login(LoginRequestDto dto, HttpServletResponse response);

    void logout(Long userPk);

    RefreshTokenResponseDto refreshAccessToken(HttpServletRequest request, HttpServletResponse response);
}
