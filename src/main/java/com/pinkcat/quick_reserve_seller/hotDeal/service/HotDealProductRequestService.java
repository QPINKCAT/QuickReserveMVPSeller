package com.pinkcat.quick_reserve_seller.hotDeal.service;

import com.pinkcat.quick_reserve_seller.hotDeal.dto.HotDealProductRequestListGetResponseDto;
import com.pinkcat.quick_reserve_seller.hotDeal.dto.HotDealProductRequestSearchCondition;
import org.springframework.data.domain.Pageable;

public interface HotDealProductRequestService {
    HotDealProductRequestListGetResponseDto getHotDealProductRequestList(HotDealProductRequestSearchCondition condition, Pageable pageable, Long userPk);
}
