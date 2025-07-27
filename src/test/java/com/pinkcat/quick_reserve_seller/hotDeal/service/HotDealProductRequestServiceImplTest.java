package com.pinkcat.quick_reserve_seller.hotDeal.service;

import com.pinkcat.quick_reserve_seller.common.exceptions.ErrorMessageCode;
import com.pinkcat.quick_reserve_seller.common.exceptions.PinkCatException;
import com.pinkcat.quick_reserve_seller.hotDeal.dto.HotDealProductRequestCreateRequestDto;
import com.pinkcat.quick_reserve_seller.hotDeal.log.HotDealProductRequestLogCommand;
import com.pinkcat.quick_reserve_seller.hotDeal.log.HotDealProductRequestLogRecoder;
import com.pinkcat.quick_reserve_seller.hotDeal.model.HotDealEntity;
import com.pinkcat.quick_reserve_seller.hotDeal.model.HotDealProductRequestEntity;
import com.pinkcat.quick_reserve_seller.hotDeal.respository.HotDealProductRequestLogRepository;
import com.pinkcat.quick_reserve_seller.hotDeal.respository.HotDealProductRequestRepository;
import com.pinkcat.quick_reserve_seller.hotDeal.respository.HotDealRepository;
import com.pinkcat.quick_reserve_seller.product.entity.ProductEntity;
import com.pinkcat.quick_reserve_seller.product.repository.ProductRepository;
import com.pinkcat.quick_reserve_seller.seller.entity.SellerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HotDealProductRequestServiceImplTest {
    @Mock
    private HotDealProductRequestRepository hotDealProductRequestRepository;
    @Mock
    private HotDealProductRequestLogRepository logRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private HotDealRepository hotDealRepository;
    @Mock
    private HotDealProductRequestLogRecoder logRecorder;

    @InjectMocks
    private HotDealProductRequestServiceImpl hotDealProductRequestService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class CreateHotDealProductRequest {
        @Test
        void 성공() {
            // given
            Long productPk = 1L;
            Long hotDealPk = 2L;
            String reason = "test reason";

            HotDealProductRequestCreateRequestDto dto = HotDealProductRequestCreateRequestDto.builder()
                    .productPk(productPk)
                    .hotDealPk(hotDealPk)
                    .reason(reason)
                    .build();
            SellerEntity seller = mock(SellerEntity.class);
            ProductEntity product = mock(ProductEntity.class);
            HotDealEntity hotDeal = mock(HotDealEntity.class);

            when(productRepository.findById(productPk)).thenReturn(Optional.of(product));
            when(hotDealRepository.findById(hotDealPk)).thenReturn(Optional.of(hotDeal));
            when(hotDealProductRequestRepository.existsByProductAndHotDeal(product, hotDeal)).thenReturn(false);

            // when
            assertDoesNotThrow(() -> hotDealProductRequestService.createHotDealProductRequest(dto, seller));

            // then
            verify(productRepository).findById(productPk);
            verify(hotDealRepository).findById(hotDealPk);
            verify(hotDealProductRequestRepository).existsByProductAndHotDeal(product, hotDeal);
            verify(hotDealProductRequestRepository).save(any(HotDealProductRequestEntity.class));
            verify(logRecorder).record(any(HotDealProductRequestLogCommand.class));
        }

        @Test
        void 실패_존재하지않는_product() {
            // given
            HotDealProductRequestCreateRequestDto dto = HotDealProductRequestCreateRequestDto.builder()
                    .productPk(999L)
                    .hotDealPk(2L)
                    .reason(null)
                    .build();
            when(productRepository.findById(999L)).thenReturn(Optional.empty());

            // when
            PinkCatException ex = assertThrows(PinkCatException.class,
                    () -> hotDealProductRequestService.createHotDealProductRequest(dto, mock(SellerEntity.class)));

            // then
            assertEquals(ErrorMessageCode.PRODUCT_NOT_FOUND_EXCEPTION, ex.getErrorMessageCode());
            verify(hotDealProductRequestRepository, never()).save(any(HotDealProductRequestEntity.class));
            verify(logRecorder, never()).record(any(HotDealProductRequestLogCommand.class));
        }

        @Test
        void 실패_존재하지않는_hotdeal() {
            ProductEntity product = mock(ProductEntity.class);
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(hotDealRepository.findById(999L)).thenReturn(Optional.empty());

            HotDealProductRequestCreateRequestDto dto = HotDealProductRequestCreateRequestDto.builder()
                    .productPk(1L)
                    .hotDealPk(999L)
                    .reason("test reason")
                    .build();

            PinkCatException ex = assertThrows(PinkCatException.class,
                    () -> hotDealProductRequestService.createHotDealProductRequest(dto, mock(SellerEntity.class)));

            assertEquals(ErrorMessageCode.HOTDEAL_NOT_FOUND_EXCEPTION, ex.getErrorMessageCode());
            verify(hotDealProductRequestRepository, never()).save(any(HotDealProductRequestEntity.class));
            verify(logRecorder, never()).record(any(HotDealProductRequestLogCommand.class));
        }

        @Test
        void 실패_중복_신청() {
            ProductEntity product = mock(ProductEntity.class);
            HotDealEntity hotDeal = mock(HotDealEntity.class);
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(hotDealRepository.findById(2L)).thenReturn(Optional.of(hotDeal));
            when(hotDealProductRequestRepository.existsByProductAndHotDeal(product, hotDeal)).thenReturn(true);

            HotDealProductRequestCreateRequestDto dto = HotDealProductRequestCreateRequestDto.builder()
                    .productPk(1L)
                    .hotDealPk(2L)
                    .reason("test reason")
                    .build();

            PinkCatException ex = assertThrows(PinkCatException.class,
                    () -> hotDealProductRequestService.createHotDealProductRequest(dto, mock(SellerEntity.class)));

            assertEquals(ErrorMessageCode.HOTDEAL_PRODUCT_REQUEST_ALREADY_EXISTS_EXCEPTION, ex.getErrorMessageCode());
            verify(hotDealProductRequestRepository, never()).save(any(HotDealProductRequestEntity.class));
            verify(logRecorder, never()).record(any(HotDealProductRequestLogCommand.class));
        }
    }
}