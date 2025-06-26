package com.antmen.antwork.common.service.serviceAccount;

import com.antmen.antwork.common.api.response.account.UserListResponseDto;
import com.antmen.antwork.common.api.response.account.UserResponseDto;
import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.api.request.account.UserLoginDto;
import com.antmen.antwork.common.domain.entity.account.UserRole;
import com.antmen.antwork.common.domain.exception.NotFoundException;
import com.antmen.antwork.common.infra.repository.account.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User login(UserLoginDto userLoginDto) {
        log.info("passwordEncoder: {}", passwordEncoder.encode(userLoginDto.getUserPassword()));

        Optional<User> optUser = userRepository.findByUserLoginId(userLoginDto.getUserLoginId());

        // 아이디 검증
        if(!optUser.isPresent()){
            throw new IllegalArgumentException("ID가 존재하지 않습니다.");
        }

        // 비밀번호 검증
        User user = optUser.get();
        if(!passwordEncoder.matches(userLoginDto.getUserPassword(), user.getUserPassword())){
            throw new IllegalArgumentException("password가 일치하지 않습니다.");
        }

        return user;
    }

    public User getUserByUserLoginId(String userLoginId, String email, String gooGle) {
        User user = userRepository.findByUserLoginId(userLoginId).orElse(null);
        return user;
    }


    /**
     * 회원 목록 조회 (페이징 처리, 역할 기반)
     */
    public List<User> searchUsers(String name, Long userId, UserRole role) {
        return userRepository.searchUsers(name, userId, role);
    }

    /**
     * 회원 단건 조회
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("해당 유저를 찾을 수 없습니다."));
    }

    public boolean existsByLoginId(String loginId) {
        return userRepository.existsByUserLoginId(loginId);
    }

    /**
     * 고객 목록 조회 (페이징 지원)
     */
    public Page<UserListResponseDto> searchCustomers(String name, Long userId, String sortBy, Pageable pageable) {
        // TODO: Repository에서 CUSTOMER 역할만 필터링하고 페이징 처리
        // sortBy에 따른 정렬 로직 추가 (현재는 DB 컬럼이 없어서 구현 제한)
        // 기본적으로는 가입일 기준으로 정렬
        return userRepository.searchCustomersWithPaging(name, userId, sortBy, pageable)
                .map(UserListResponseDto::toListDto);
    }

    @Async
    public void updateLastLoginAsync(Long userId) {
        try {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null && shouldUpdateLastLogin(user.getLastLoginAt())) {
                user.setLastLoginAt(LocalDateTime.now());
                userRepository.save(user);
            }
        } catch (Exception e) {
            log.warn("마지막 로그인 시간 업데이트 실패: userId={}", userId, e);
        }
    }

    private boolean shouldUpdateLastLogin(LocalDateTime lastLoginAt) {
        return lastLoginAt == null ||
                lastLoginAt.isBefore(LocalDateTime.now().minusHours(1));
    }

}
