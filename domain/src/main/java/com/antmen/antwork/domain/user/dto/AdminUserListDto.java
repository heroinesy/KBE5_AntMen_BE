package com.antmen.antwork.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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