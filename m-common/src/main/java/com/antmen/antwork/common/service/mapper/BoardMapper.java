package com.antmen.antwork.common.service.mapper;

import com.antmen.antwork.common.api.request.board.BoardRequestDto;
import com.antmen.antwork.common.api.response.board.BoardListResponseDto;
import com.antmen.antwork.common.api.response.board.BoardResponseDto;
import com.antmen.antwork.common.api.response.board.CommentResponseDto;
import com.antmen.antwork.common.domain.entity.Board;
import com.antmen.antwork.common.domain.entity.account.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BoardMapper {

    private final CommentMapper commentMapper;

    public Board toEntity(BoardRequestDto boardRequestDto, String boardType, User user) {
        if (boardRequestDto == null) {
            return null;
        }

        return Board.builder()
                .boardUser(user)
                .boardType(boardType)
                .boardTitle(boardRequestDto.getBoardTitle())
                .boardContent(boardRequestDto.getBoardContent())
                .boardCreatedAt(LocalDateTime.now())
                .boardModifiedAt(LocalDateTime.now())
                .isPinned(boardRequestDto.getBoardIsPinned() != null ? boardRequestDto.getBoardIsPinned() : false)
                .boardIsDeleted(false)
                .build();
    }

    public BoardResponseDto toResponseDto(Board board) {
        if (board == null) {
            return null;
        }

        List<CommentResponseDto> commentDtos;
        
        if (board.getComments() != null) {
            commentDtos = board.getComments().stream()
                    .filter(comment -> comment.getParentComment() == null)
                    .map(commentMapper::toResponseDto)
                    .collect(Collectors.toList());
        } else {
            commentDtos = Collections.emptyList();
        }

        return BoardResponseDto.builder()
                .boardId(board.getBoardId())
                .userName(board.getBoardUser() != null ? board.getBoardUser().getUserName() : null)
                .boardTitle(board.getBoardTitle())
                .boardContent(board.getBoardContent())
                .createdAt(board.getBoardCreatedAt())
                .modifiedAt(board.getBoardModifiedAt())
                .comments(commentDtos)
                .build();
    }

    public BoardListResponseDto toListResponseDto(Board board) {
        if (board == null) {
            return null;
        }

        int commentCount = 0;
        if (board.getComments() != null) {
            commentCount = board.getComments().size();
        }

        return BoardListResponseDto.builder()
                .boardId(board.getBoardId())
                .userName(board.getBoardUser() != null ? board.getBoardUser().getUserName() : null)
                .boardTitle(board.getBoardTitle())
                .createdAt(board.getBoardCreatedAt())
                .modifiedAt(board.getBoardModifiedAt())
                .isPinned(board.getIsPinned())
                .isDeleted(board.getBoardIsDeleted())
                .commentNum(commentCount)
                .build();
    }
}
