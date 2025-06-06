package com.antmen.antwork.common.service.serviceAccount;

import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.api.request.account.UserLoginDto;
import com.antmen.antwork.common.infra.repository.account.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
