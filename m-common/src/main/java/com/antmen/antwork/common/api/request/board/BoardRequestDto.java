package com.antmen.antwork.common.api.request.board;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class BoardRequestDto {
    private String boardTitle;
    private String boardContent;
    private Boolean boardIsPinned;
}
