package com.pinkcat.quick_reserve_seller.hotdeal.service;

import com.pinkcat.quick_reserve_seller.hotdeal.dto.HotDealGetResponseDto;
import com.pinkcat.quick_reserve_seller.hotdeal.dto.HotDealListGetResponseDto;
import org.springframework.data.domain.Pageable;

public interface HotDealService {

    HotDealGetResponseDto getHotDeal(Long hotDealPk);

    HotDealListGetResponseDto getHotDealList(Pageable pageable);
}
