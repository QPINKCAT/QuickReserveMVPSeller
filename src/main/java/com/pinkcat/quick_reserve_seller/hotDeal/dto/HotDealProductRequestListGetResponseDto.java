package com.pinkcat.quick_reserve_seller.hotDeal.dto;


import com.pinkcat.quick_reserve_seller.hotDeal.model.HotDealProductRequestLogEntity;
import com.pinkcat.quick_reserve_seller.hotDeal.model.HotDealProductRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotDealProductRequestListGetResponseDto {

    private List<HotDealProductRequest> content;

    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public static HotDealProductRequestListGetResponseDto fromPage(Page<HotDealProductRequestLogEntity> page) {
        return HotDealProductRequestListGetResponseDto.builder()
                .content(page.getContent().stream()
                        .map(HotDealProductRequest::fromEntity)
                        .collect(Collectors.toList()))
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class HotDealProductRequest {
        private Long hotDealProductRequestPk;
        private String reason;
        private HotDealProductRequestStatus status;
        private Long createdAt;

        // hotdeal
        private Long hotDealPk;
        private String hotDealName;

        // product
        private Long productPk;
        private String productName;
        private Integer productPrice;

        public static HotDealProductRequest fromEntity(HotDealProductRequestLogEntity entity) {
            return HotDealProductRequest.builder()
                    .hotDealProductRequestPk(entity.getPk())
                    .reason(entity.getReason())
                    .status(entity.getHotDealProductRequestStatus())
                    .createdAt(entity.getCreatedAt())

                    .hotDealPk(entity.getHotDeal().getPk())
                    .hotDealName(entity.getHotDeal().getName())

                    .productPk(entity.getProduct().getPk())
                    .productName(entity.getProduct().getName())
                    .productPrice(entity.getProduct().getPrice())
                    .build();
        }

    }
}