package com.pinkcat.quick_reserve_seller.hotDeal.controller;


import com.pinkcat.quick_reserve_seller.common.security.principal.UserPrincipal;
import com.pinkcat.quick_reserve_seller.hotDeal.dto.*;
import com.pinkcat.quick_reserve_seller.hotDeal.service.HotDealProductRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hotdeal-product-requests")
@RequiredArgsConstructor
public class HotDealProductRequestController {
    private final HotDealProductRequestService hotDealProductRequestService;

    @GetMapping
    public ResponseEntity<HotDealProductRequestListGetResponseDto> getHotDealProductRequestList(
            @ModelAttribute HotDealProductRequestSearchCondition condition,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        HotDealProductRequestListGetResponseDto response = hotDealProductRequestService.getHotDealProductRequestList(condition, pageable, user.getUserPk());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<HotDealProductRequestCreateResponseDto> createHotDealProductRequest(
            @RequestBody @Valid HotDealProductRequestCreateRequestDto dto,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        HotDealProductRequestCreateResponseDto response = hotDealProductRequestService.createHotDealProductRequest(dto, user.getUser());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> cancelHotDealProductRequest(
            @RequestBody HotDealProductRequestCancelRequestDto dto,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        hotDealProductRequestService.cancelHotDealProductRequest(dto, user.getUser());
        return ResponseEntity.ok().build();
    }
}
