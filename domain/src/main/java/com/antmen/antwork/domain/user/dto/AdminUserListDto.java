package com.antmen.antwork.domain.user.dto;








@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserListDto {
    private List<AdminUserResponseDto> content;
    private Long totalElements;
    private Integer totalPages;
    private Integer currentPage;
} 