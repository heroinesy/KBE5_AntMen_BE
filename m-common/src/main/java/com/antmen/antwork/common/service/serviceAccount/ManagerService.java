package com.antmen.antwork.common.service.serviceAccount;

import com.antmen.antwork.common.api.request.account.ManagerSignupRequestDto;
import com.antmen.antwork.common.api.response.account.ManagerResponseDto;
import com.antmen.antwork.common.domain.entity.account.ManagerDetail;
import com.antmen.antwork.common.domain.entity.account.ManagerIdFile;
import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.domain.entity.account.UserRole;
import com.antmen.antwork.common.infra.repository.account.ManagerDetailRepository;
import com.antmen.antwork.common.infra.repository.account.ManagerIdFileRepository;
import com.antmen.antwork.common.infra.repository.account.UserRepository;
import com.antmen.antwork.common.service.mapper.account.ManagerMapper;
import com.antmen.antwork.common.util.S3UploaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final UserRepository userRepository;
    private final ManagerDetailRepository managerDetailRepository;
    private final ManagerIdFileRepository managerIdFileRepository;
    private final ManagerMapper managerMapper;
    private final S3UploaderService s3UploaderService;

    @Transactional
    public ManagerResponseDto signUp(
            ManagerSignupRequestDto managerSignupRequestDto) throws IOException {

        if (userRepository.findByUserLoginId(managerSignupRequestDto.getUserLoginId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        MultipartFile profileFile = managerSignupRequestDto.getUserProfile();
        if (profileFile == null || profileFile.isEmpty()) {
            throw new IllegalArgumentException("프로필 이미지를 첨부해주세요.");
        }

        String profileUrl = s3UploaderService.upload(profileFile, "manager-profile");

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
                        String url = s3UploaderService.upload(file, "manager-id-files");
                        return managerIdFileRepository.save(managerMapper.toManagerIdFileEntity(user, url));
                    } catch (IOException e) {
                        throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.");
                    }
                })
                .collect(Collectors.toList());


        return managerMapper.toDto(user, managerDetail, managerIdFiles);

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
}
