package com.antmen.antwork.common.service.mapper.account;

import com.antmen.antwork.common.api.request.account.ManagerSignupRequestDto;
import com.antmen.antwork.common.api.response.account.ManagerIdFileDto;
import com.antmen.antwork.common.api.response.account.ManagerResponseDto;
import com.antmen.antwork.common.domain.entity.account.ManagerDetail;
import com.antmen.antwork.common.domain.entity.account.ManagerIdFile;
import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.domain.entity.account.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequestInterceptor;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ManagerMapper {
    private final PasswordEncoder passwordEncoder;
    private final WebRequestInterceptor webRequestInterceptor;

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
                .managerStatus(managerDetail.getManagerStatus())
                .rejectReason(managerDetail.getRejectReason())
                .build();
    }

    public User toUserEntity(ManagerSignupRequestDto request, String profileUrl){

        String encodedPassword = null;
        // 일반 가입일 경우에만 비밀번호 암호화
        if (request.getUserType() == null) {
            if (request.getUserPassword() == null || request.getUserPassword().isBlank()) {
                throw new IllegalArgumentException("일반 회원가입 시에는 비밀번호가 필수입니다.");
            }
            encodedPassword = passwordEncoder.encode(request.getUserPassword());
        }

        return User.builder()
                .userLoginId(request.getUserLoginId())
                .userPassword(encodedPassword)
                .userName(request.getUserName())
                .userTel(request.getUserTel())
                .userEmail(request.getUserEmail())
                .userGender(request.getUserGender())
                .userBirth(request.getUserBirth())
                .userProfile(profileUrl)
                .userType(request.getUserType())
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
