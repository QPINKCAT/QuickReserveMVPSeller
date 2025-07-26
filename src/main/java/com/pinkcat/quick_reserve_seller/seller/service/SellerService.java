package com.pinkcat.quick_reserve_seller.seller.service;

import com.pinkcat.quick_reserve_seller.seller.dto.SellerGetResponseDto;

public interface SellerService {
  SellerGetResponseDto getMyInfo(Long userPk);
}
