package com.pinkcat.quick_reserve_seller.hotDeal.service;

import com.pinkcat.quick_reserve_seller.common.security.principal.UserPrincipal;
import com.pinkcat.quick_reserve_seller.hotDeal.dto.*;
import com.pinkcat.quick_reserve_seller.seller.entity.SellerEntity;
import org.springframework.data.domain.Pageable;

public interface HotDealProductRequestService {
    HotDealProductRequestListGetResponseDto getHotDealProductRequestList(HotDealProductRequestSearchCondition condition, Pageable pageable, Long userPk);

    HotDealProductRequestCreateResponseDto createHotDealProductRequest(HotDealProductRequestCreateRequestDto dto, SellerEntity user);

    void cancelHotDealProductRequest(HotDealProductRequestCancelRequestDto dto, SellerEntity user);
}
