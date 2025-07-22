package com.antmen.antwork.domain.board.dto;









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
