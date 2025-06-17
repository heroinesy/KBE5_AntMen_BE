package com.antmen.antwork.common.api.response.board;

import com.antmen.antwork.common.domain.entity.Board;
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
    private String userName;
    private String boardTitle;
    private String boardContent;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> comments;

    public BoardResponseDto toDto(Board board, String userName) {
        BoardResponseDto boardResponseDto = new BoardResponseDto();

        boardResponseDto.setBoardId(board.getBoardId());
        boardResponseDto.setUserName(userName);
        boardResponseDto.setBoardTitle(board.getBoardTitle());
        boardResponseDto.setBoardContent(board.getBoardContent());
        boardResponseDto.setCreatedAt(board.getBoardCreatedAt());
        boardResponseDto.setModifiedAt(board.getBoardModifiedAt());
        // 댓글 리스트

        return boardResponseDto;

    }
}
