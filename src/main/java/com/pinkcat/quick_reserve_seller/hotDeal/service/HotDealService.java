package com.pinkcat.quick_reserve_seller.hotDeal.service;

import com.pinkcat.quick_reserve_seller.hotDeal.dto.HotDealGetResponseDto;
import com.pinkcat.quick_reserve_seller.hotDeal.dto.HotDealListGetResponseDto;
import org.springframework.data.domain.Pageable;

public interface HotDealService {

    HotDealGetResponseDto getHotDeal(Long hotDealPk);

    HotDealListGetResponseDto getHotDealList(Pageable pageable);
}
