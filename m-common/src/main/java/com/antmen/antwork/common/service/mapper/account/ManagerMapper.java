package com.antmen.antwork.common.service.mapper.account;

import com.antmen.antwork.common.api.request.account.ManagerSignupRequestDto;
import com.antmen.antwork.common.api.request.account.ManagerUpdateRequestDto;
import com.antmen.antwork.common.api.response.account.ManagerIdFileDto;
import com.antmen.antwork.common.api.response.account.ManagerResponseDto;
import com.antmen.antwork.common.api.response.account.ManagerWatingListDto;
import com.antmen.antwork.common.domain.entity.account.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequestInterceptor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ManagerMapper {
    private final PasswordEncoder passwordEncoder;
    private final WebRequestInterceptor webRequestInterceptor;
    private final ManagerIdFileMapper managerIdFileMapper;

    public ManagerResponseDto toDto(ManagerDetail managerDetail, List<ManagerIdFile> managerIdFiles) {
        return ManagerResponseDto.builder()
                .userName(managerDetail.getUser().getUserName())
                .userTel(managerDetail.getUser().getUserTel())
                .userEmail(managerDetail.getUser().getUserEmail())
                .userGender(managerDetail.getUser().getUserGender() == UserGender.M? "남성" : "여성")
                .userBirth(managerDetail.getUser().getUserBirth())
                .userProfile(managerDetail.getUser().getUserProfile())
                .managerAddress(managerDetail.getManagerAddress())
//                .managerLatitude(managerDetail.getManagerLatitude())
//                .managerLongitude(managerDetail.getManagerLongitude())
                .managerTime(managerDetail.getManagerTime())
                .managerFileUrls(managerIdFiles.stream()
                        .map(
                        file -> managerIdFileMapper.toDto(file)
                        )
                        .collect(Collectors.toList()))
                .managerStatus(managerDetail.getManagerStatus())
                .rejectReason(managerDetail.getRejectReason())
                .userType(managerDetail.getUser().getUserType())
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
                .isBlack(false)
                .build();
    }

    public ManagerDetail toManagerDetailEntity(User user, ManagerSignupRequestDto request){
        return ManagerDetail.builder()
                .user(user)
                .managerAddress(request.getManagerAddress())
                .managerLatitude(request.getManagerLatitude())
                .managerLongitude(request.getManagerLongitude())
                .managerTime(request.getManagerTime())
                .managerStatus(ManagerStatus.WAITING)
                .build();
    }

    public void updateUserFromDto(User user, ManagerUpdateRequestDto dto) {
        user.setUserName(dto.getUserName());
        user.setUserTel(dto.getUserTel());
        user.setUserEmail(dto.getUserEmail());
        user.setUserGender(dto.getUserGender());
        user.setUserBirth(dto.getUserBirth());
    }

    public void updateManagerDetailFromDto(ManagerDetail detail, ManagerUpdateRequestDto dto) {
        detail.setManagerAddress(dto.getManagerAddress());
        detail.setManagerLatitude(dto.getManagerLatitude());
        detail.setManagerLongitude(dto.getManagerLongitude());
        detail.setManagerTime(dto.getManagerTime());
    }

    public ManagerWatingListDto toWaitingDto(ManagerDetail managerDetail) {
        return ManagerWatingListDto.builder()
                .userId(managerDetail.getUserId())
                .userName(managerDetail.getUser().getUserName())
                .userAge(managerDetail.getUser().getUserBirth().until(LocalDate.now()).getYears())
                .userGender(managerDetail.getUser().getUserGender() == UserGender.M ? "남성" : "여성")
                .userAddress(managerDetail.getManagerAddress())
                .userCreatedAt(managerDetail.getUser().getUserCreatedAt())
                .managerStatus(managerDetail.getManagerStatus())
                .build();
    }
}
