package com.pinkcat.quick_reserve_seller.hotdeal.dto;

import com.pinkcat.quick_reserve_seller.common.enums.HotDealPublicStatusEnum;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotDealGetResponseDto {
    private Long hotDealPk;
    private String name;
    private String description;
    private String thumbnail;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private HotDealPublicStatusEnum publicStatus;
    private List<Long> productPks;
}
