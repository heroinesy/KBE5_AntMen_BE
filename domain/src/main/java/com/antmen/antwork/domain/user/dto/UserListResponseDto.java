package com.antmen.antwork.domain.user.dto;









@Getter
@Setter
@Builder
public class UserListResponseDto {
    private Long userId;
    private String userName;
    private String userEmail;
    private String userTel;
    private UserRole userRole;
    private LocalDateTime userCreatedDate;
    private LocalDateTime lastReservationDate;
    private Boolean isBlack;
    private String blacklistReason;
    private LocalDateTime blacklistDate;

    public static UserListResponseDto toListDto(User user) {
        return UserListResponseDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userEmail(user.getUserEmail())
                .userTel(user.getUserTel())
                .userRole(user.getUserRole())
                .userCreatedDate(user.getUserCreatedAt())
                .lastReservationDate(user.getLastReservationAt())
                .isBlack(user.getIsBlack())
                .blacklistReason(user.getBlacklistReason())
                .blacklistDate(user.getBlacklistDate())
                .build();
    }
}
