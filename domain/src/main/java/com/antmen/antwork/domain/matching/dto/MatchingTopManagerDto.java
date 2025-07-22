package com.antmen.antwork.domain.matching.dto;






@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchingTopManagerDto {
    private Long managerId;
    private String managerName;
    private Long successCount;
}