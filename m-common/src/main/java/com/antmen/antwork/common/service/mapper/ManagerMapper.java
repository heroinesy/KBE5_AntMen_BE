package com.antmen.antwork.common.service.mapper;

import com.antmen.antwork.common.api.request.ManagerSignupRequestDto;
import com.antmen.antwork.common.api.response.ManagerIdFileDto;
import com.antmen.antwork.common.api.response.ManagerResponseDto;
import com.antmen.antwork.common.domain.entity.ManagerDetail;
import com.antmen.antwork.common.domain.entity.ManagerIdFile;
import com.antmen.antwork.common.domain.entity.User;
import com.antmen.antwork.common.domain.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ManagerMapper {
    private final PasswordEncoder passwordEncoder;

    public ManagerResponseDto toDto(User user, ManagerDetail managerDetail, List<ManagerIdFile> managerIdFiles) {
        return ManagerResponseDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userTel(user.getUserTel())
                .userEmail(user.getUserEmail())
                .userGender(user.getUserGender())
                .userBirth(user.getUserBirth())
                .userProfile(user.getUserProfile())
                .managerAddress(managerDetail.getManagerAddress())
                .managerArea(managerDetail.getManagerArea())
                .managerTime(managerDetail.getManagerTime())
                .managerFileUrls(managerIdFiles.stream()
                        .map(file -> ManagerIdFileDto.builder()
                                .id(file.getManagerFileId())
                                .fileUrl(file.getManagerFileUrl())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public User toUserEntity(ManagerSignupRequestDto request, String profileUrl){
        return User.builder()
                .userLoginId(request.getUserLoginId())
                .userPassword(passwordEncoder.encode(request.getUserPassword()))
                .userName(request.getUserName())
                .userTel(request.getUserTel())
                .userEmail(request.getUserEmail())
                .userGender(request.getUserGender())
                .userBirth(request.getUserBirth())
                .userProfile(profileUrl)
                .userRole(UserRole.MANAGER)
                .build();
    }

    public ManagerDetail toManagerDetailEntity(User user, ManagerSignupRequestDto request){
        return ManagerDetail.builder()
                .user(user)
                .managerAddress(request.getManagerAddress())
                .managerArea(request.getManagerArea())
                .managerTime(request.getManagerTime())
                .build();
    }

    public ManagerIdFile toManagerIdFileEntity(User user, String fileUrl){
        return ManagerIdFile.builder()
                .user(user)
                .managerFileUrl(fileUrl)
                .build();

    }
}
