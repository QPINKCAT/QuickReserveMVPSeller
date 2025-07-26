package com.pinkcat.quick_reserve_seller.seller.controller;

import com.pinkcat.quick_reserve_seller.common.security.principal.UserPrincipal;
import com.pinkcat.quick_reserve_seller.seller.dto.SellerGetResponseDto;
import com.pinkcat.quick_reserve_seller.seller.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mypage/me")
@RequiredArgsConstructor
public class SellerController {
    private final SellerService sellerService;

    @GetMapping
    public ResponseEntity<SellerGetResponseDto> getMyInfo(
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(sellerService.getMyInfo(user.getUserPk()));
    }
}
