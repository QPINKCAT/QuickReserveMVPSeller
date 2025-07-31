package com.pinkcat.quick_reserve_seller.auth.controller;

import com.pinkcat.quick_reserve_seller.auth.dto.LoginRequestDto;
import com.pinkcat.quick_reserve_seller.auth.dto.LoginResponseDto;
import com.pinkcat.quick_reserve_seller.auth.service.AuthService;
import com.pinkcat.quick_reserve_seller.common.security.principal.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserPrincipal user) {
        authService.logout(user.getUsername());
        return ResponseEntity.ok().build();
    }
}
