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
    private final RefreshTokenCookieProvider refreshTokenCookieProvider;
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

        refreshTokenStore.save(user.getPk(), refreshToken);

        Cookie rtCookie = refreshTokenCookieProvider.createRefreshTokenCookie(refreshToken);
        response.addCookie(rtCookie);

        log.info("[로그인 성공] userId={}", user.getId());
        return LoginResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    @Override
    public void logout(Long userPk) {
        refreshTokenStore.delete(userPk);
        log.info("[로그아웃 성공] userId={}", userPk);
    }

    @Override
    public RefreshTokenResponseDto refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie rtCookie = WebUtils.getCookie(request, "refresh_token");
        if (rtCookie == null) {
            log.warn("[토큰 재발급 실패] RefreshToken 쿠키가 존재하지 않습니다.");
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "RefreshToken 쿠키가 존재하지 않습니다.");
        }

        String refreshToken = rtCookie.getValue();

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            log.warn("[토큰 재발급 실패] RefreshToken이 유효하지 않습니다.");
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "RefreshToken이 유효하지 않습니다.");
        }

        Long userPk = jwtTokenProvider.getUserPk(refreshToken);

        if (!refreshTokenStore.isValid(userPk, refreshToken)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "RefreshToken 정보가 일치하지 않습니다.");
        }

        String newAccessToken = jwtTokenProvider.createAccessToken(userPk);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(userPk);

        refreshTokenStore.save(userPk, newRefreshToken);
        Cookie newRtCookie = refreshTokenCookieProvider.createRefreshTokenCookie(newRefreshToken);
        response.addCookie(newRtCookie);

        return new RefreshTokenResponseDto(newAccessToken);
    }
}
