package com.antmen.antwork.common.service.serviceAccount;

import com.antmen.antwork.common.api.request.account.ManagerSignupRequestDto;
import com.antmen.antwork.common.api.response.account.ManagerIdFileDto;
import com.antmen.antwork.common.api.response.account.ManagerResponseDto;
import com.antmen.antwork.common.domain.entity.ReviewSummary;
import com.antmen.antwork.common.domain.entity.account.*;
import com.antmen.antwork.common.domain.exception.NotFoundException;
import com.antmen.antwork.common.infra.repository.account.ManagerDetailRepository;
import com.antmen.antwork.common.infra.repository.account.ManagerIdFileRepository;
import com.antmen.antwork.common.infra.repository.account.UserRepository;
import com.antmen.antwork.common.infra.repository.reservation.ReviewSummaryRepository;
import com.antmen.antwork.common.service.mapper.account.ManagerIdFileMapper;
import com.antmen.antwork.common.service.mapper.account.ManagerMapper;
import com.antmen.antwork.common.util.S3UploaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerService {

    private final UserRepository userRepository;
    private final ManagerDetailRepository managerDetailRepository;
    private final ManagerIdFileRepository managerIdFileRepository;
    private final ManagerMapper managerMapper;
    private final S3UploaderService s3UploaderService;
    private final ReviewSummaryRepository reviewSummaryRepository;
    private final ManagerIdFileMapper managerIdFileMapper;

    @Transactional
    public ManagerResponseDto signUp(
            ManagerSignupRequestDto managerSignupRequestDto) throws IOException {

        List<String> uploadedFileUrls = new ArrayList<>();
        try {
        if (userRepository.findByUserLoginId(managerSignupRequestDto.getUserLoginId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        MultipartFile profileFile = managerSignupRequestDto.getUserProfile();
        if (profileFile == null || profileFile.isEmpty()) {
            throw new IllegalArgumentException("프로필 이미지를 첨부해주세요.");
        }

        String profileUrl = s3UploaderService.upload(profileFile, "manager-profile");
        uploadedFileUrls.add(profileUrl);

        User user = managerMapper.toUserEntity(managerSignupRequestDto, profileUrl);
        userRepository.save(user);

        ManagerDetail managerDetail = managerMapper.toManagerDetailEntity(user, managerSignupRequestDto);
        managerDetailRepository.save(managerDetail);

        if (managerSignupRequestDto.getManagerFileUrls() == null || managerSignupRequestDto.getManagerFileUrls().isEmpty()) {
            throw new IllegalArgumentException("신원 확인을 위한 파일은 최소 1개 이상 필요합니다.");
        }

        List<ManagerIdFile> managerIdFiles = managerSignupRequestDto.getManagerFileUrls().stream()
                .filter(file -> file != null && !file.isEmpty())
                .map(file -> {
                    try {
                        ManagerIdFileDto dto = s3UploaderService.uploadWithMeta(file, "manager-id-files");
                        uploadedFileUrls.add(dto.getManagerFileUrl());
                        return managerIdFileRepository.save(managerIdFileMapper.toEntity(user, dto));
                    } catch (IOException e) {
                        throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.");
                    }
                })
                .collect(Collectors.toList());

        return managerMapper.toDto(user, managerDetail, managerIdFiles);

        } catch (Exception e) {
            // 예외 발생 시 업로드한 파일 모두 삭제
            for (String url : uploadedFileUrls) {
                try {
                    s3UploaderService.deleteFile(url);
                } catch (Exception ex) {
                    log.error("S3 파일 삭제 실패: {}", url, ex);
                }
            }
            throw e;
        }

    }

    @Transactional(readOnly = true)
    public List<ManagerResponseDto> getManagers() {

        List<User> managerList = userRepository.findByUserRole(UserRole.MANAGER);

        return managerList.stream()
                .map(user -> {
                    ManagerDetail detail = managerDetailRepository.findByUser(user)
                            .orElseThrow(() -> new IllegalStateException("해당 매니저의 상세 정보가 없습니다."));

                    List<ManagerIdFile> idFiles = managerIdFileRepository.findAllByUser(user);

                    return managerMapper.toDto(user, detail, idFiles);
                })
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public ManagerResponseDto getManager(Long id) {

        User user = userRepository.findById(id)
                .filter(u -> u.getUserRole() == UserRole.MANAGER)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 매니저입니다."));

        ManagerDetail detail = managerDetailRepository.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("해당 매니저의 상세 정보가 없습니다."));

        List<ManagerIdFile> idFiles = managerIdFileRepository.findAllByUser(user);

        return managerMapper.toDto(user, detail, idFiles);
    }

    /**
     * 승인 대기 중인 매니저 조회
     */
    @Transactional(readOnly = true)
    public List<ManagerResponseDto> getWaitingManagers() {
        List<User> managerList = userRepository.findByUserRole(UserRole.MANAGER);

        return managerList.stream()
                .filter(user -> {
                    ManagerDetail detail = managerDetailRepository.findByUser(user).orElse(null);
                    return detail != null && detail.getManagerStatus() == ManagerStatus.WAITING;
                })
                .map(user -> {
                    ManagerDetail detail = managerDetailRepository.findByUser(user).get();
                    List<ManagerIdFile> idFiles = managerIdFileRepository.findAllByUser(user);
                    return managerMapper.toDto(user, detail, idFiles);
                })
                .collect(Collectors.toList());
    }

    /**
     * 매니저 가입 승인
     */
    @Transactional
    public void approveManager(Long id) {
        User user = userRepository.findById(id)
                .filter(u -> u.getUserRole() == UserRole.MANAGER)
                .orElseThrow(() -> new NotFoundException("매니저를 찾을 수 없습니다."));

        ManagerDetail detail = managerDetailRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("매니저 상세 정보가 없습니다."));

        detail.setManagerStatus(ManagerStatus.APPROVED);
        detail.setRejectReason(null);
        reviewSummaryRepository.save(ReviewSummary.builder()
                        .managerId(id)
                        .totalReviews(0L)
                        .avgRating(0.0f)
                .build());
    }

    /**
     * 매니저 가입 거절
     * @param id
     * @param reason
     */
    @Transactional
    public void rejectManager(Long id, String reason) {
        User user = userRepository.findById(id)
                .filter(u -> u.getUserRole() == UserRole.MANAGER)
                .orElseThrow(() -> new NotFoundException("매니저를 찾을 수 없습니다."));

        ManagerDetail detail = managerDetailRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("매니저 상세 정보가 없습니다."));

        detail.setManagerStatus(ManagerStatus.REJECTED);
        detail.setRejectReason(reason);
    }
}
