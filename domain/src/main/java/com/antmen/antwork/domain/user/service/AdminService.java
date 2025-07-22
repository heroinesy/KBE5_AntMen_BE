package com.antmen.antwork.domain.user.service;












@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminJwtTokenProvider adminJwtTokenProvider;
    public static final String INITIAL_ADMIN_PASSWORD = "admin4885";

    public AdminTokenDto login(UserLoginDto userLoginDto) {
        User user = userRepository.findByUserLoginId(userLoginDto.getUserLoginId())
                .orElseThrow(() -> new IllegalArgumentException("잘못 입력하셨습니다."));

        if (user.getUserRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }

        if (!passwordEncoder.matches(userLoginDto.getUserPassword(), user.getUserPassword())) {
            throw new IllegalArgumentException("잘못 입력하셨습니다.");
        }

        String token = adminJwtTokenProvider.createToken(user.getUserId());
        long expiresIn = adminJwtTokenProvider.getExpiration() / 1000;
        boolean isUsingInitialPassword = passwordEncoder.matches(INITIAL_ADMIN_PASSWORD, user.getUserPassword());

        return new AdminTokenDto(token, expiresIn, isUsingInitialPassword);
    }

    public void changePassword(Long adminId, AdminPasswordChangeDto adminPasswordChangeDto) {
        log.info("adminId: {}, adminPasswordChangeDto: {}", adminId, adminPasswordChangeDto);
        User user = userRepository.findByUserId(adminId);
        log.info("user: {}", user);

        if (user.getUserRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }

        if (!passwordEncoder.matches(adminPasswordChangeDto.getCurrentPassword(), user.getUserPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        user.setUserPassword(passwordEncoder.encode(adminPasswordChangeDto.getNewPassword()));
    }
}