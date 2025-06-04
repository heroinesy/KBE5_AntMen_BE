package com.antmen.antwork.common.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponseDto {
    private Long commentId;
    private String userName;
    private String commentContent;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> subComments;
} 