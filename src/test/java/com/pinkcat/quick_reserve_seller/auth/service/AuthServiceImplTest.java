package com.pinkcat.quick_reserve_seller.auth.service;

import com.pinkcat.quick_reserve_seller.auth.dto.LoginRequestDto;
import com.pinkcat.quick_reserve_seller.auth.dto.LoginResponseDto;
import com.pinkcat.quick_reserve_seller.common.redis.RefreshTokenStore;
import com.pinkcat.quick_reserve_seller.common.security.jwt.JwtTokenProvider;
import com.pinkcat.quick_reserve_seller.seller.entity.SellerEntity;
import com.pinkcat.quick_reserve_seller.seller.repository.SellerRepository;
import com.pinkcat.quick_reserve_seller.store.entity.StoreEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthServiceImplTest {

    @Mock
    private SellerRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private RefreshTokenStore refreshTokenStore;

    @InjectMocks
    private AuthServiceImpl authService;

    private SellerEntity userEntity;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        userEntity =
                new SellerEntity("testuser",
                        "테스트유저",
                        "encoded_pass",
                        "01012341234",
                        "testuser@test.com",
                        new StoreEntity());
        userEntity.setPk(1L);
    }

    @Nested
    class LoginTest {

        @Test
        void 성공() {
            // given
            LoginRequestDto loginDto = new LoginRequestDto("testuser", "rawpass");
            when(userRepository.findById("testuser")).thenReturn(Optional.of(userEntity));
            when(passwordEncoder.matches("rawpass", "encoded_pass")).thenReturn(true);
            when(jwtTokenProvider.createAccessToken(1L)).thenReturn("access-token");
            when(jwtTokenProvider.createRefreshToken(1L)).thenReturn("refresh-token");

            // when
            LoginResponseDto result = authService.login(loginDto);

            // then
            assertEquals("access-token", result.getAccessToken());
            assertEquals("refresh-token", result.getRefreshToken());
            verify(refreshTokenStore).save("testuser", "refresh-token");
        }

        @Test
        void 실패_존재하지않는ID() {
            // given
            LoginRequestDto loginDto = new LoginRequestDto("wronguser", "rawpass");
            when(userRepository.findById("wronguser")).thenReturn(Optional.empty());

            // when
            ResponseStatusException ex =
                    assertThrows(ResponseStatusException.class, () -> authService.login(loginDto));

            // then
            assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
        }

        @Test
        void 실패_비밀번호불일치() {
            // given
            LoginRequestDto loginDto = new LoginRequestDto("testuser", "wrongpass");
            when(userRepository.findById("testuser")).thenReturn(Optional.of(userEntity));
            when(passwordEncoder.matches("wrongpass", "encoded_pass")).thenReturn(false);

            // when
            ResponseStatusException ex =
                    assertThrows(ResponseStatusException.class, () -> authService.login(loginDto));

            // then
            assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
        }

        @Test
        void 실패_비활성화된계정() {
            // given
            LoginRequestDto loginDto = new LoginRequestDto("testuser", "rawpass");

            userEntity.setActive(false);
            when(userRepository.findById("testuser")).thenReturn(Optional.of(userEntity));

            // when
            ResponseStatusException ex =
                    assertThrows(ResponseStatusException.class, () -> authService.login(loginDto));

            // then
            assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
        }

    }

    @Nested
    class LogoutTest {

        @Test
        void 성공() {
            // given
            String userId = "testuser";

            // when
            assertDoesNotThrow(() -> authService.logout(userId));

            // then
            verify(refreshTokenStore).delete(userId);
        }
    }
}
