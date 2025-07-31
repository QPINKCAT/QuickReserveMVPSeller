package com.pinkcat.quick_reserve_seller.seller.controller;

import com.pinkcat.quick_reserve_seller.common.security.principal.UserPrincipal;
import com.pinkcat.quick_reserve_seller.seller.dto.SellerGetResponseDto;
import com.pinkcat.quick_reserve_seller.seller.dto.SellerUpdatePasswordRequestDto;
import com.pinkcat.quick_reserve_seller.seller.service.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/password")
    public ResponseEntity<Void> updateMyPassword(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody @Valid SellerUpdatePasswordRequestDto dto) {
        sellerService.updatePassword(user.getUserPk(), dto);
        return ResponseEntity.ok().build();
    }
}
