package com.pinkcat.quick_reserve_seller.hotDeal.controller;

import com.pinkcat.quick_reserve_seller.hotDeal.dto.HotDealGetResponseDto;
import com.pinkcat.quick_reserve_seller.hotDeal.dto.HotDealListGetResponseDto;
import com.pinkcat.quick_reserve_seller.hotDeal.service.HotDealService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hotdeals")
@RequiredArgsConstructor
public class HotDealController {

    private final HotDealService hotDealService;

    @GetMapping("/{hotDealPk}")
    public ResponseEntity<HotDealGetResponseDto> getHotDeal(
            @PathVariable Long hotDealPk
    ) {
        return ResponseEntity.ok(hotDealService.getHotDeal(hotDealPk));
    }

    @GetMapping
    public ResponseEntity<HotDealListGetResponseDto> getHotDealList(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(hotDealService.getHotDealList(pageable));
    }

}
