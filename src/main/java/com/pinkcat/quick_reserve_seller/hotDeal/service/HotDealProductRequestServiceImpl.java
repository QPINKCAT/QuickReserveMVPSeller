package com.pinkcat.quick_reserve_seller.hotDeal.service;

import com.pinkcat.quick_reserve_seller.common.exceptions.ErrorMessageCode;
import com.pinkcat.quick_reserve_seller.common.exceptions.PinkCatException;
import com.pinkcat.quick_reserve_seller.common.security.principal.UserPrincipal;
import com.pinkcat.quick_reserve_seller.hotDeal.dto.*;
import com.pinkcat.quick_reserve_seller.hotDeal.log.HotDealProductRequestLogCommand;
import com.pinkcat.quick_reserve_seller.hotDeal.log.HotDealProductRequestLogRecoder;
import com.pinkcat.quick_reserve_seller.hotDeal.model.HotDealEntity;
import com.pinkcat.quick_reserve_seller.hotDeal.model.HotDealProductRequestEntity;
import com.pinkcat.quick_reserve_seller.hotDeal.model.HotDealProductRequestLogEntity;
import com.pinkcat.quick_reserve_seller.hotDeal.model.HotDealProductRequestStatus;
import com.pinkcat.quick_reserve_seller.hotDeal.respository.HotDealProductRequestLogRepository;
import com.pinkcat.quick_reserve_seller.hotDeal.respository.HotDealProductRequestRepository;
import com.pinkcat.quick_reserve_seller.hotDeal.respository.HotDealRepository;
import com.pinkcat.quick_reserve_seller.product.entity.ProductEntity;
import com.pinkcat.quick_reserve_seller.product.repository.ProductRepository;
import com.pinkcat.quick_reserve_seller.seller.entity.SellerEntity;
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
    private final HotDealProductRequestRepository hotDealProductRequestRepository;
    private final HotDealProductRequestLogRepository hotDealProductRequestLogRepository;
    private final ProductRepository productRepository;
    private final HotDealRepository hotDealRepository;
    private final HotDealProductRequestLogRecoder logRecoder;

    @Override
    @Transactional(readOnly = true)
    public HotDealProductRequestListGetResponseDto getHotDealProductRequestList(HotDealProductRequestSearchCondition condition, Pageable pageable, Long userPk) {
        Page<HotDealProductRequestLogEntity> page =
                hotDealProductRequestLogRepository.search(condition, pageable, userPk);

        return HotDealProductRequestListGetResponseDto.fromPage(page);
    }

    @Override
    @Transactional
    public HotDealProductRequestCreateResponseDto createHotDealProductRequest(HotDealProductRequestCreateRequestDto dto, SellerEntity user) {
        ProductEntity product = productRepository.findById(dto.getProductPk())
                .orElseThrow(() -> {
                    log.warn("[핫딜 상품 신청 실패] 존재하지 않는 상품입니다. productPk={}", dto.getProductPk());
                    throw new PinkCatException("존재하지 않는 상품입니다.", ErrorMessageCode.PRODUCT_NOT_FOUND_EXCEPTION);
                });

        HotDealEntity hotDeal = hotDealRepository.findById(dto.getHotDealPk())
                .orElseThrow(() -> {
                    log.warn("[핫딜 상품 신청 실패] 존재하지 않는 핫딜입니다. hotDealPk={}", dto.getHotDealPk());
                    throw new PinkCatException("존재하지 않는 핫딜입니다.", ErrorMessageCode.HOTDEAL_NOT_FOUND_EXCEPTION);
                });

        boolean exists = hotDealProductRequestRepository.existsByProductAndHotDeal(product, hotDeal);
        if (exists) {
            log.warn("[핫딜 상품 신청 실패] 이미 해당 핫딜에 상품 요청이 존재합니다. productPk={}, hotDealPk={}", product.getPk(), hotDeal.getPk());
            throw new PinkCatException("이미 해당 핫딜에 대한 상품 요청이 존재합니다.", ErrorMessageCode.HOTDEAL_PRODUCT_REQUEST_ALREADY_EXISTS_EXCEPTION);
        }

        HotDealProductRequestEntity hotDealProductRequest = new HotDealProductRequestEntity(
                hotDeal,
                product,
                dto.getReason()
        );
        hotDealProductRequestRepository.save(hotDealProductRequest);


        HotDealProductRequestLogCommand cmd = HotDealProductRequestLogCommand.of(
                product,
                hotDeal,
                HotDealProductRequestStatus.REQUESTED,
                user,
                null,
                dto.getReason()
        );

        log.info("[핫딜 상품 신청 성공] sellerPk={}, productPk={}, hotDealPk={}, reason={}",
                user.getId(), product.getPk(), hotDeal.getPk(), dto.getReason());
        logRecoder.record(cmd);
        return HotDealProductRequestCreateResponseDto.fromEntity(hotDealProductRequest);
    }

    @Override
    @Transactional
    public void cancelHotDealProductRequest(HotDealProductRequestCancelRequestDto dto, SellerEntity user) {
        ProductEntity product = productRepository.findById(dto.getProductPk())
                .orElseThrow(() -> {
                    log.warn("[핫딜 상품 취소 실패] 존재하지 않는 상품입니다. productPk={}", dto.getProductPk());
                    throw new PinkCatException("존재하지 않는 상품입니다.", ErrorMessageCode.PRODUCT_NOT_FOUND_EXCEPTION);
                });

        HotDealEntity hotDeal = hotDealRepository.findById(dto.getHotDealPk())
                .orElseThrow(() -> {
                    log.warn("[핫딜 상품 취소 실패] 존재하지 않는 핫딜입니다. hotDealPk={}", dto.getHotDealPk());
                    throw new PinkCatException("존재하지 않는 핫딜입니다.", ErrorMessageCode.HOTDEAL_NOT_FOUND_EXCEPTION);
                });

        HotDealProductRequestEntity request = hotDealProductRequestRepository
                .findByProductAndHotDeal(product, hotDeal)
                .orElseThrow(() -> {
                    log.warn("[핫딜 상품 취소 실패] 신청 내역이 존재하지 않거나 이미 처리된 상태입니다. productPk={}, hotDealPk={}", product.getPk(), hotDeal.getPk());
                    throw new PinkCatException("해당 상품에 대한 신청 내역이 존재하지 않거나 이미 처리되었습니다.", ErrorMessageCode.HOTDEAL_PRODUCT_REQUEST_NOT_FOUND_EXCEPTION);
                });


        hotDealProductRequestRepository.delete(request);

        logRecoder.record(HotDealProductRequestLogCommand.of(
                product,
                hotDeal,
                HotDealProductRequestStatus.CANCELLED,
                user,
                null,
                dto.getReason()
        ));

        log.info("[핫딜 상품 취소 성공] productPk={}, hotDealPk={}",
                 product.getPk(), hotDeal.getPk());
    }

}
