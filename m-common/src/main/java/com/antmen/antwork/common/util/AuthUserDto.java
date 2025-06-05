package com.antmen.antwork.common.util;


import com.antmen.antwork.common.domain.entity.account.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@RequiredArgsConstructor
public class AuthUserDto implements UserDetails {

    private final String userId;

    private final UserRole userRole;

    // ========== UserDetails 인터페이스 구현 ==========

    /**
     * Spring Security에서 사용하는 사용자명
     * 여기서는 userId를 username으로 사용
     *
     * @return 사용자 고유 ID
     */
    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + userRole.name())
        );
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
