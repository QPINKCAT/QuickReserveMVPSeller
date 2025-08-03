package com.pinkcat.quick_reserve_seller.hotDeal.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotDealProductRequestCreateRequestDto {
    @NotNull
    private Long hotDealPk;

    @NotNull
    private Long productPk;

    private String reason;
}
