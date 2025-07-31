package com.pinkcat.quick_reserve_seller.seller.service;

import com.pinkcat.quick_reserve_seller.common.exceptions.ErrorMessageCode;
import com.pinkcat.quick_reserve_seller.common.exceptions.PinkCatException;
import com.pinkcat.quick_reserve_seller.seller.dto.SellerUpdatePasswordRequestDto;
import com.pinkcat.quick_reserve_seller.seller.entity.SellerEntity;
import com.pinkcat.quick_reserve_seller.seller.repository.SellerRepository;
import com.pinkcat.quick_reserve_seller.store.entity.StoreEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SellerServiceImplTest {
    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SellerServiceImpl sellerService;

    private SellerEntity activeSeller;
    private SellerEntity inactiveSeller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        activeSeller = new SellerEntity("activetestuser",
                "활성화테스트유저",
                "encoded_pass",
                "01012341234",
                "activetest@example.com",
                new StoreEntity());
        activeSeller.setPk(1L);

        inactiveSeller = new SellerEntity("inactivetestuser",
                "비활성화테스트유저",
                "encoded_pass",
                "01012341234",
                "inactivetest@example.com",
                new StoreEntity());
        inactiveSeller.setActive(false);
        inactiveSeller.setPk(2L);

    }

    @Nested
    class UpdatePasswordTest {

        @Test
        void 성공() {
            // given
            Long sellerPk = activeSeller.getPk();
            String currentRaw = "oldPassword";
            String newRaw = "newPassword";

            when(sellerRepository.findByPkAndActiveTrue(sellerPk))
                    .thenReturn(Optional.of(activeSeller));
            when(passwordEncoder.matches(currentRaw, activeSeller.getPassword())).thenReturn(true);
            when(passwordEncoder.matches(newRaw, activeSeller.getPassword())).thenReturn(false);
            when(passwordEncoder.encode(newRaw)).thenReturn("encodedNewPassword");

            // when
            sellerService.updatePassword(
                    sellerPk, new SellerUpdatePasswordRequestDto(currentRaw, newRaw));

            // then
            verify(sellerRepository).save(activeSeller);
            assertEquals("encodedNewPassword", activeSeller.getPassword());
        }

        @Test
        void 실패_새비밀번호누락_null() {
            // given
            Long sellerPk = activeSeller.getPk();
            String currentRaw = "oldPassword";
            String newRaw = null;

            when(sellerRepository.findByPkAndActiveTrue(sellerPk))
                    .thenReturn(Optional.of(activeSeller));
            when(passwordEncoder.matches(currentRaw, activeSeller.getPassword())).thenReturn(true);

            // when
            PinkCatException ex = assertThrows(
                    PinkCatException.class, () ->
                            sellerService.updatePassword(
                                    sellerPk, new SellerUpdatePasswordRequestDto(currentRaw, newRaw)));

            // then
            assertEquals(ErrorMessageCode.SELLER_INVALID_PASSWORD_EXCEPTION, ex.getPinkCatErrorMessageCode());
        }

        @Test
        void 실패_새비밀번호누락_blank() {
            // given
            Long sellerPk = activeSeller.getPk();
            String currentRaw = "oldPassword";
            String newRaw = " ";

            when(sellerRepository.findByPkAndActiveTrue(sellerPk))
                    .thenReturn(Optional.of(activeSeller));
            when(passwordEncoder.matches(currentRaw, activeSeller.getPassword())).thenReturn(true);

            // when
            PinkCatException ex = assertThrows(PinkCatException.class, () ->
                    sellerService.updatePassword(
                            sellerPk, new SellerUpdatePasswordRequestDto(currentRaw, newRaw)));

            // then
            assertEquals(ErrorMessageCode.SELLER_INVALID_PASSWORD_EXCEPTION, ex.getPinkCatErrorMessageCode());
        }

        @Test
        void 실패_새비밀번호가기존과동일() {
            // given
            Long sellerPk = activeSeller.getPk();
            String currentRaw = "samePassword";
            String newRaw = "samePassword";

            when(sellerRepository.findByPkAndActiveTrue(sellerPk))
                    .thenReturn(Optional.of(activeSeller));
            when(passwordEncoder.matches(currentRaw, activeSeller.getPassword())).thenReturn(true);
            when(passwordEncoder.matches(newRaw, activeSeller.getPassword())).thenReturn(true);

            // when
            PinkCatException ex = assertThrows(PinkCatException.class, () ->
                    sellerService.updatePassword(
                            sellerPk, new SellerUpdatePasswordRequestDto(currentRaw, newRaw)));

            // then
            assertEquals(ErrorMessageCode.SELLER_INVALID_PASSWORD_EXCEPTION, ex.getPinkCatErrorMessageCode());
        }

        @Test
        void 실패_현재비밀번호불일치() {
            // given
            Long sellerPk = activeSeller.getPk();
            String currentRaw = "wrongPassword";
            String newRaw = "newPassword";

            when(sellerRepository.findByPkAndActiveTrue(sellerPk))
                    .thenReturn(Optional.of(activeSeller));
            when(passwordEncoder.matches(currentRaw, activeSeller.getPassword())).thenReturn(false);

            // when
            PinkCatException ex = assertThrows(PinkCatException.class, () ->
                    sellerService.updatePassword(
                            sellerPk, new SellerUpdatePasswordRequestDto(currentRaw, newRaw)));

            // then
            assertEquals(ErrorMessageCode.SELLER_INVALID_PASSWORD_EXCEPTION, ex.getPinkCatErrorMessageCode());
        }

        @Test
        void 실패_비활성화계정() {
            // given
            Long sellerPk = inactiveSeller.getPk();
            String currentRaw = "password";
            String newRaw = "newPassword";

            when(sellerRepository.findByPkAndActiveTrue(sellerPk)).thenReturn(Optional.empty());

            // when
            PinkCatException ex = assertThrows(PinkCatException.class, () ->
                    sellerService.updatePassword(
                            sellerPk, new SellerUpdatePasswordRequestDto(currentRaw, newRaw)));

            // then
            assertEquals(ErrorMessageCode.SELLER_INACTIVE_EXCEPTION, ex.getPinkCatErrorMessageCode());
        }
    }
}