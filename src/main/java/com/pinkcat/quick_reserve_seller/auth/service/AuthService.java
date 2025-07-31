package com.pinkcat.quick_reserve_seller.auth.service;


import com.pinkcat.quick_reserve_seller.auth.dto.LoginRequestDto;
import com.pinkcat.quick_reserve_seller.auth.dto.LoginResponseDto;

public interface AuthService {

    LoginResponseDto login(LoginRequestDto dto);

    void logout(Long userPk);

    RefreshTokenResponseDto refreshAccessToken(HttpServletRequest request, HttpServletResponse response);
}
