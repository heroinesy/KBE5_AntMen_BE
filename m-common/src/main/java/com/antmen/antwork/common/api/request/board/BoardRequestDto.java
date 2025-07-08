package com.antmen.antwork.common.api.request.board;

import com.antmen.antwork.common.domain.entity.BoardStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
public class BoardRequestDto {
    private String boardTitle;
    private String boardContent;
    private Boolean boardIsPinned;
    private LocalDateTime boardReservatedAt;
    private BoardStatus boardStatus;
}
