package com.pinkcat.quick_reserve_seller.common.security.principal;

import com.pinkcat.quick_reserve_seller.seller.entity.SellerEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class UserPrincipal implements UserDetails {

    private final SellerEntity user;

    public UserPrincipal(SellerEntity user) {
        this.user = user;
    }

    public Long getUserPk() {
        return user.getPk();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // Role 없는 구조
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getId(); // 로그인 ID
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
