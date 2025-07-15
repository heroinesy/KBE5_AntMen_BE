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

        User user = optUser.get();

        if (user.getUserRole() == UserRole.ADMIN) {
            throw new IllegalArgumentException("관리자는 접근할 수 없습니다.");
        }

        // 비밀번호 검증
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
    public Page<UserListResponseDto> searchCustomers(String name, String sortBy, Pageable pageable) {
        // TODO: Repository에서 CUSTOMER 역할만 필터링하고 페이징 처리
        // sortBy에 따른 정렬 로직 추가 (현재는 DB 컬럼이 없어서 구현 제한)
        // 기본적으로는 가입일 기준으로 정렬

        return userRepository.searchCustomersWithPaging(name, sortBy, pageable)
                .map(UserListResponseDto::toListDto);
    }

    /**
     * 블랙리스트 회원 목록 조회
     */
    public Page<UserListResponseDto> getBlacklistUsers(String name, String userRole, Pageable pageable) {
        UserRole role = null;
        if (userRole != null && !userRole.isEmpty()) {
            try {
                role = UserRole.valueOf(userRole);
            } catch (IllegalArgumentException e) {
                log.warn("Invalid userRole: {}", userRole);
            }
        }
        return userRepository.findBlacklistUsers(name, role, pageable)
                .map(UserListResponseDto::toListDto);
    }

    /**
     * 회원을 블랙리스트에 추가
     */
    public void addToBlacklist(Long userId, String reason) {
        User user = getUserById(userId);
        
        // 관리자는 블랙리스트에 추가할 수 없음
        if (user.getUserRole() == UserRole.ADMIN) {
            throw new IllegalArgumentException("관리자는 블랙리스트에 추가할 수 없습니다.");
        }
        
        user.setIsBlack(true);
        user.setBlacklistReason(reason);
        user.setBlacklistDate(LocalDateTime.now());
        userRepository.save(user);
        
        log.info("User {} added to blacklist. Reason: {}", userId, reason);
    }

    /**
     * 회원을 블랙리스트에서 제거
     */
    public void removeFromBlacklist(Long userId) {
        User user = getUserById(userId);
        user.setIsBlack(false);
        user.setBlacklistReason(null);
        user.setBlacklistDate(null);
        userRepository.save(user);
        
        log.info("User {} removed from blacklist.", userId);
    }

}
