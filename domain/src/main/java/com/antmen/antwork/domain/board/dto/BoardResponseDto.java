package com.antmen.antwork.domain.board.dto;

import com.antmen.antwork.common.domain.entity.Board;
import com.antmen.antwork.common.domain.entity.BoardStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardResponseDto {
    private Long boardId;
    private Long userId;
    private String userName;
    private String boardTitle;
    private String boardContent;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private BoardStatus boardStatus;
    private Boolean isPinned;
    private List<CommentResponseDto> comments;
}
