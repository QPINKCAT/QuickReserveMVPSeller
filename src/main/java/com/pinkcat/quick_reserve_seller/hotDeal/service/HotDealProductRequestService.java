package com.pinkcat.quick_reserve_seller.hotDeal.service;

import com.pinkcat.quick_reserve_seller.hotDeal.dto.HotDealProductRequestCreateRequestDto;
import com.pinkcat.quick_reserve_seller.hotDeal.dto.HotDealProductRequestCreateResponseDto;
import com.pinkcat.quick_reserve_seller.hotDeal.dto.HotDealProductRequestListGetResponseDto;
import com.pinkcat.quick_reserve_seller.hotDeal.dto.HotDealProductRequestSearchCondition;
import com.pinkcat.quick_reserve_seller.seller.entity.SellerEntity;
import org.springframework.data.domain.Pageable;

public interface HotDealProductRequestService {
    HotDealProductRequestListGetResponseDto getHotDealProductRequestList(HotDealProductRequestSearchCondition condition, Pageable pageable, Long userPk);

    HotDealProductRequestCreateResponseDto createHotDealProductRequest(HotDealProductRequestCreateRequestDto dto, SellerEntity user);
}
