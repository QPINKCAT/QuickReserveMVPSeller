package com.pinkcat.quick_reserve_seller.hotDeal.dto;

import com.pinkcat.quick_reserve_seller.hotDeal.model.HotDealProductRequestEntity;
import com.pinkcat.quick_reserve_seller.hotDeal.model.HotDealProductRequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotDealProductRequestCreateResponseDto {
    private Long requestPk;

    public static HotDealProductRequestCreateResponseDto fromEntity(HotDealProductRequestEntity entity) {
        return HotDealProductRequestCreateResponseDto.builder()
                .requestPk(entity.getPk())
                .build();
    }
}
