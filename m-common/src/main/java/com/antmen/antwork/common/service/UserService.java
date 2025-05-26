package com.antmen.antwork.common.service;

import com.antmen.antwork.common.domain.entity.User;
import com.antmen.antwork.common.dto.UserCreateDto;
import com.antmen.antwork.common.infra.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(UserCreateDto userCreateDto) {
        User user = User.builder()
                .userEmail(userCreateDto.getUserEmail())
                .userPassword(passwordEncoder.encode(userCreateDto.getUserPassword()))
                .build();

        userRepository.save(user);

        return user;
    }
}
