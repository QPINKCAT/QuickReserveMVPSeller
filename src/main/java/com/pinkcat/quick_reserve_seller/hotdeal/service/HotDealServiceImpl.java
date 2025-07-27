package com.pinkcat.quick_reserve_seller.hotdeal.service;

import com.pinkcat.quick_reserve_seller.common.enums.HotDealPublicStatusEnum;
import com.pinkcat.quick_reserve_seller.common.exceptions.ErrorMessageCode;
import com.pinkcat.quick_reserve_seller.common.exceptions.PinkCatException;
import com.pinkcat.quick_reserve_seller.hotDeal.model.HotDealEntity;
import com.pinkcat.quick_reserve_seller.hotDeal.respository.HotDealRepository;
import com.pinkcat.quick_reserve_seller.hotdeal.dto.HotDealGetResponseDto;
import com.pinkcat.quick_reserve_seller.hotdeal.dto.HotDealListGetResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class HotDealServiceImpl implements HotDealService {

    private final HotDealRepository hotDealRepository;

    @Override
    public HotDealGetResponseDto getHotDeal(Long hotDealPk) {
        HotDealEntity hotDeal = hotDealRepository.findById(hotDealPk)
                .orElseThrow(() -> new PinkCatException(
                        "존재하지 않는 핫딜입니다.",
                        ErrorMessageCode.HOTDEAL_NOT_FOUND_EXCEPTION
                ));

        if (hotDeal.getPublicStatus() == HotDealPublicStatusEnum.ADMIN_ONLY) {
            throw new PinkCatException("접근할 수 없는 핫딜입니다.",
                    ErrorMessageCode.HOTDEAL_ACCESS_DENIED_EXCEPTION);
        }

        List<Long> productPks = hotDeal.getHotDealProducts().stream()
                .map(hdp -> hdp.getProduct().getPk())
                .toList();

        return HotDealGetResponseDto.builder()
                .hotDealPk(hotDeal.getPk())
                .name(hotDeal.getName())
                .description(hotDeal.getDescription())
                .thumbnail(hotDeal.getThumbnail())
                .startAt(hotDeal.getStartAt())
                .endAt(hotDeal.getEndAt())
                .publicStatus(hotDeal.getPublicStatus())
                .productPks(productPks)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public HotDealListGetResponseDto getHotDealList(Pageable pageable) {
        Page<HotDealEntity> page = hotDealRepository.findAllByPublicStatusNot(
                HotDealPublicStatusEnum.ADMIN_ONLY, pageable
        );

        List<HotDealListGetResponseDto.HotDeal> content = page.getContent().stream()
                .map(hotDeal -> HotDealListGetResponseDto.HotDeal.builder()
                        .hotDealPk(hotDeal.getPk())
                        .name(hotDeal.getName())
                        .description(hotDeal.getDescription())
                        .thumbnail(hotDeal.getThumbnail())
                        .startAt(hotDeal.getStartAt())
                        .endAt(hotDeal.getEndAt())
                        .publicStatus(hotDeal.getPublicStatus())
                        .build())
                .toList();

        return HotDealListGetResponseDto.builder()
                .content(content)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}
