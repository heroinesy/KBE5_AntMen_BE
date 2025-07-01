package com.antmen.antwork.common.service.serviceAccount;

import com.antmen.antwork.common.api.request.AdminPasswordChangeDto;
import com.antmen.antwork.common.api.request.account.UserLoginDto;
import com.antmen.antwork.common.api.response.account.AdminTokenDto;
import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.domain.entity.account.UserRole;
import com.antmen.antwork.common.infra.repository.account.UserRepository;
import com.antmen.antwork.common.util.AdminJwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminJwtTokenProvider adminJwtTokenProvider;
    public static final String INITIAL_ADMIN_PASSWORD = "admin4885";

    public AdminTokenDto login(UserLoginDto userLoginDto) {
        User user = userRepository.findByUserLoginId(userLoginDto.getUserLoginId())
                .orElseThrow(() -> new IllegalArgumentException("잘못 입력하셨습니다."));

        if (user.getUserRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }

        if (!passwordEncoder.matches(userLoginDto.getUserPassword(), user.getUserPassword())) {
            throw new IllegalArgumentException("잘못 입력하셨습니다.");
        }

        String token = adminJwtTokenProvider.createToken(user.getUserId());
        long expiresIn = adminJwtTokenProvider.getExpiration() / 1000;
        boolean isUsingInitialPassword = passwordEncoder.matches(INITIAL_ADMIN_PASSWORD, user.getUserPassword());

        return new AdminTokenDto(token, expiresIn, isUsingInitialPassword);
    }

    public void changePassword(Long adminId, AdminPasswordChangeDto adminPasswordChangeDto) {
        log.info("adminId: {}, adminPasswordChangeDto: {}", adminId, adminPasswordChangeDto);
        User user = userRepository.findByUserId(adminId);
        log.info("user: {}", user);

        if (user.getUserRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }

        if (!passwordEncoder.matches(adminPasswordChangeDto.getCurrentPassword(), user.getUserPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        user.setUserPassword(passwordEncoder.encode(adminPasswordChangeDto.getNewPassword()));
    }
}
