package com.pinkcat.quick_reserve_seller.seller.service;

import com.pinkcat.quick_reserve_seller.common.exceptions.ErrorMessageCode;
import com.pinkcat.quick_reserve_seller.common.exceptions.PinkCatException;
import com.pinkcat.quick_reserve_seller.seller.dto.SellerGetResponseDto;
import com.pinkcat.quick_reserve_seller.seller.dto.SellerUpdatePasswordRequestDto;
import com.pinkcat.quick_reserve_seller.seller.entity.SellerEntity;
import com.pinkcat.quick_reserve_seller.seller.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {
    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;

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

    @Transactional
    public void updatePassword(Long sellerPk, SellerUpdatePasswordRequestDto dto) {
        SellerEntity seller =
                sellerRepository
                        .findByPkAndActiveTrue(sellerPk)
                        .orElseThrow(
                                () -> {
                                    log.warn("[비밀번호 변경 실패] 비활성화 계정/계정 없음: sellerPk={}", sellerPk);
                                    return new PinkCatException(
                                            "비활성화된 계정입니다. 관리자에게 문의해주세요.", ErrorMessageCode.SELLER_INACTIVE_EXCEPTION);
                                });

        if (dto.getNewPassword() == null || dto.getNewPassword().isBlank()) {
            log.warn("[비밀번호 변경 실패] 새 비밀번호 값 누락: sellerPk={}", sellerPk);
            throw new PinkCatException("비밀번호는 필수 값입니다.", ErrorMessageCode.SELLER_INVALID_PASSWORD_EXCEPTION);
        }

        if (passwordEncoder.matches(dto.getNewPassword(), seller.getPassword())) {
            log.warn("[비밀번호 변경 실패] 새 비밀번호 기존과 동일: sellerPk={}", sellerPk);
            throw new PinkCatException("새 비밀번호가 기존과 같습니다.", ErrorMessageCode.SELLER_INVALID_PASSWORD_EXCEPTION);
        }

        if (!passwordEncoder.matches(dto.getPassword(), seller.getPassword())) {
            log.warn("[비밀번호 변경 실패] 기존 비밀번호 불일치: sellerPk={}", sellerPk);
            throw new PinkCatException("현재 비밀번호가 일치하지 않습니다.", ErrorMessageCode.SELLER_INVALID_PASSWORD_EXCEPTION);
        }

        seller.updateEncodedPassword(passwordEncoder.encode(dto.getNewPassword()));

        log.info("[비밀번호 변경 성공] sellerPk={}", sellerPk);

        sellerRepository.save(seller);
    }
}
