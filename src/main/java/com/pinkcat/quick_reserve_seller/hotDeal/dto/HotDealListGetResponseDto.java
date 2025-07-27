package com.pinkcat.quick_reserve_seller.hotDeal.dto;

import com.pinkcat.quick_reserve_seller.common.enums.HotDealPublicStatusEnum;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotDealListGetResponseDto {
    private List<HotDeal> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HotDeal {
        private Long hotDealPk;
        private String name;
        private String description;
        private String thumbnail;
        private LocalDateTime startAt;
        private LocalDateTime endAt;
        private HotDealPublicStatusEnum publicStatus;
    }
}
