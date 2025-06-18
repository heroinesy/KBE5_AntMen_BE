package com.antmen.antwork.common.service.mapper;

import com.antmen.antwork.common.api.request.board.BoardRequestDto;
import com.antmen.antwork.common.api.response.board.BoardListResponseDto;
import com.antmen.antwork.common.api.response.board.BoardResponseDto;
import com.antmen.antwork.common.api.response.board.CommentResponseDto;
import com.antmen.antwork.common.domain.entity.Board;
import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.infra.repository.board.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class BoardMapper {

    public Board toEntity(BoardRequestDto boardRequestDto, String boardType, Long userId ) {
        if (boardRequestDto == null) {
            log.error("BoardRequestDto is null");
            return null;
        }

        return Board.builder()
                .boardUserId(userId)
                .boardType(boardType)
                .boardTitle(boardRequestDto.getBoardTitle())
                .boardContent(boardRequestDto.getBoardContent())
                .boardCreatedAt(LocalDateTime.now())
                .boardModifiedAt(LocalDateTime.now())
                .boardReservedAt(boardRequestDto.getBoardReservatedAt())
                .isPinned(boardRequestDto.getBoardIsPinned() != null ? boardRequestDto.getBoardIsPinned() : false)
                .boardIsDeleted(false)
                .build();
    }

//    public BoardResponseDto toResponseDto(Board board) {
//        if (board == null) {
//            return null;
//        }
//
//        List<CommentResponseDto> commentDtos;
//
//        if (board.getComments() != null) {
//            commentDtos = board.getComments().stream()
//                    .filter(comment -> comment.getParentComment() == null)
//                    .map(commentMapper::toResponseDto)
//                    .collect(Collectors.toList());
//        } else {
//            commentDtos = Collections.emptyList();
//        }
//
//        return BoardResponseDto.builder()
//                .boardId(board.getBoardId())
//                .userName(board.getBoardUser() != null ? board.getBoardUser().getUserName() : null)
//                .boardTitle(board.getBoardTitle())
//                .boardContent(board.getBoardContent())
//                .createdAt(board.getBoardCreatedAt())
//                .modifiedAt(board.getBoardModifiedAt())
//                .comments(commentDtos)
//                .build();
//    }

    public BoardListResponseDto toListResponseDto(Board board, Long commentCount, String userName) {
        if (board == null) {
            return null;
        }

        return BoardListResponseDto.builder()
                .boardId(board.getBoardId())
                .userName(userName)
                .boardTitle(board.getBoardTitle())
                .createdAt(board.getBoardCreatedAt())
                .modifiedAt(board.getBoardModifiedAt())
                .isPinned(board.getIsPinned())
                .isDeleted(board.getBoardIsDeleted())
                .commentNum(commentCount)
                .build();
    }
}
