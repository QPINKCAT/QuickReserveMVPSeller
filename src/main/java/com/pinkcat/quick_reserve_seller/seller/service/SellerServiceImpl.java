package com.pinkcat.quick_reserve_seller.seller.service;

import com.pinkcat.quick_reserve_seller.common.exceptions.ErrorMessageCode;
import com.pinkcat.quick_reserve_seller.common.exceptions.PinkCatException;
import com.pinkcat.quick_reserve_seller.seller.dto.SellerGetResponseDto;
import com.pinkcat.quick_reserve_seller.seller.entity.SellerEntity;
import com.pinkcat.quick_reserve_seller.seller.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {
    private final SellerRepository sellerRepository;

    @Override
    public SellerGetResponseDto getMyInfo(Long userPk) {
        SellerEntity seller =
                sellerRepository
                        .findByPkAndActiveTrue(userPk)
                        .orElseThrow(
                                () -> {
                                    log.warn("[내 정보 조회 실패] 비활성화 계정/계정 없음: userPk={}", userPk);
                                    throw new PinkCatException(
                                            "비활성화된 계정입니다. 관리자에게 문의해주세요.", ErrorMessageCode.SELLER_INACTIVE_EXCEPTION);
                                });

        return SellerGetResponseDto.fromEntity(seller);
    }
}
