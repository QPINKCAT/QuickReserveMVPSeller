package com.pinkcat.quick_reserve_seller.seller.service;

import com.pinkcat.quick_reserve_seller.seller.dto.SellerGetResponseDto;
import com.pinkcat.quick_reserve_seller.seller.dto.SellerUpdatePasswordRequestDto;

public interface SellerService {
  SellerGetResponseDto getMyInfo(Long userPk);

  void updatePassword(Long userPk, SellerUpdatePasswordRequestDto dto);
}
