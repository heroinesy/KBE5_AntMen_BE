package com.antmen.antwork.common.api.controller;

import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.domain.entity.account.UserRole;
import com.antmen.antwork.common.infra.repository.account.UserRepository;
import com.antmen.antwork.common.util.JwtTokenProvider;  // 실제 JwtTokenProvider 사용
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestUserController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;  // 실제 JwtTokenProvider 주입

    /**
     * 테스트용 전체 사용자 조회
     */
    @GetMapping("/user")
    public ResponseEntity<List<TestUserDto>> getAllUsers() {
        return ResponseEntity.ok(
                userRepository.findAll().stream()
                        .map(TestUserDto::toDto)
                        .toList()
        );
    }

    /**
     * 테스트용 토큰 발급 (userId만 받아서, userRole은 DB에서 조회)
     */
    @PostMapping("/auth/token")
    public ResponseEntity<TestTokenResponse> createTestToken(@RequestBody TestTokenRequest request) {
        // 1. userId로 DB에서 User 조회
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        // 2. userRole을 DB에서 꺼냄
        UserRole userRole = user.getUserRole();

        // 3. 토큰 생성
        String token = jwtTokenProvider.createToken(
                user.getUserId(),
                userRole.name()
        );

        return ResponseEntity.ok(new TestTokenResponse(token));
    }

    // DTO 클래스들
    @Getter @Setter @Builder
    public static class TestUserDto {
        private Long userId;
        private String userName;
        private UserRole userRole;

        public static TestUserDto toDto(User user) {
            return TestUserDto.builder()
                    .userId(user.getUserId())
                    .userName(user.getUserName())
                    .userRole(user.getUserRole())
                    .build();
        }
    }

    @Getter @Setter
    public static class TestTokenRequest {
        private Long userId;
    }

    @Getter @AllArgsConstructor
    public static class TestTokenResponse {
        private String token;
    }
}