package com.antmen.antwork.domain.board.dto;

import com.antmen.antwork.common.domain.entity.BoardStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardListResponseDto {
    private Long boardId;
    private String userName;
    private String boardTitle;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long commentNum;
    private BoardStatus boardStatus;
    private Boolean isDeleted;


}
