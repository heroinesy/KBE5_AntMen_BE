package com.antmen.antwork.domain.board.dto;









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
