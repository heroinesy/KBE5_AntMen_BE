package com.antmen.antwork.common.api.response.board;

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
public class BoardResponseDto {
    private Long boardId;
    private String userName;
    private String boardTitle;
    private String boardContent;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> comments;
}
