package com.pinkcat.quick_reserve_seller.common.security.jwt;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenCookieProvider {

    @Value("${jwt.cookie.refresh-token.path}")
    private String refreshTokenCookiePath;

    @Value("${jwt.cookie.refresh-token.secure}")
    private boolean refreshTokenCookieSecure;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    public Cookie createRefreshTokenCookie(String token) {
        Cookie cookie = new Cookie("refresh_token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(refreshTokenCookieSecure);
        cookie.setPath(refreshTokenCookiePath);
        cookie.setMaxAge((int) (refreshExpiration / 1000));
        return cookie;
    }
}
