package com.pinkcat.quick_reserve_seller.auth.service;

import com.pinkcat.quick_reserve_seller.auth.dto.LoginRequestDto;
import com.pinkcat.quick_reserve_seller.auth.dto.LoginResponseDto;
import com.pinkcat.quick_reserve_seller.common.redis.RefreshTokenStore;
import com.pinkcat.quick_reserve_seller.common.security.jwt.JwtTokenProvider;
import com.pinkcat.quick_reserve_seller.seller.entity.SellerEntity;
import com.pinkcat.quick_reserve_seller.seller.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SellerRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenStore refreshTokenStore;

    @Override
    public LoginResponseDto login(LoginRequestDto dto) {
        SellerEntity user =
                userRepository
                        .findById(dto.getUserId())
                        .filter(SellerEntity::getActive)
                        .filter(c -> passwordEncoder.matches(dto.getPassword(), c.getPassword()))
                        .orElseThrow(
                                () -> {
                                    log.warn("[로그인 실패] ID/비밀번호 불일치 or 비활성화 계정: userId={}", dto.getUserId());
                                    return new ResponseStatusException(
                                            HttpStatus.UNAUTHORIZED, "사용자 ID 또는 비밀번호가 올바르지 않습니다.");
                                });

        String accessToken = jwtTokenProvider.createAccessToken(user.getPk());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getPk());

        refreshTokenStore.save(user.getId(), refreshToken);
        log.info("[로그인 성공] userId={}", user.getId());
        return LoginResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    @Override
    public void logout(String userId) {
        refreshTokenStore.delete(userId);
        log.info("[로그아웃 성공] userId={}", userId);
    }
}
