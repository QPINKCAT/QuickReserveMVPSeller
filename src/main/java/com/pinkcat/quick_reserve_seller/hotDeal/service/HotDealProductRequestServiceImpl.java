package com.pinkcat.quick_reserve_seller.hotDeal.service;

import com.pinkcat.quick_reserve_seller.hotDeal.model.HotDealProductRequestLogEntity;
import com.pinkcat.quick_reserve_seller.hotDeal.respository.HotDealProductRequestLogRepository;
import com.pinkcat.quick_reserve_seller.hotDeal.dto.HotDealProductRequestListGetResponseDto;
import com.pinkcat.quick_reserve_seller.hotDeal.dto.HotDealProductRequestSearchCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class HotDealProductRequestServiceImpl implements HotDealProductRequestService {
    private final HotDealProductRequestLogRepository hotDealProductRequestLogRepository;
    @Transactional(readOnly = true)
    @Override
    public HotDealProductRequestListGetResponseDto getHotDealProductRequestList(HotDealProductRequestSearchCondition condition, Pageable pageable, Long userPk) {
        Page<HotDealProductRequestLogEntity> page =
                hotDealProductRequestLogRepository.search(condition, pageable, userPk);

        return HotDealProductRequestListGetResponseDto.fromPage(page);
    }
}
