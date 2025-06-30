package com.antmen.antwork.common.service.mapper;

import com.antmen.antwork.common.api.request.board.BoardRequestDto;
import com.antmen.antwork.common.api.response.board.BoardListResponseDto;
import com.antmen.antwork.common.api.response.board.BoardResponseDto;
import com.antmen.antwork.common.api.response.board.CommentResponseDto;
import com.antmen.antwork.common.domain.entity.Board;
import com.antmen.antwork.common.domain.entity.Comment;
import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.infra.repository.account.UserRepository;
import com.antmen.antwork.common.infra.repository.board.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
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

    private final UserRepository userRepository;

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
                .isFinished(false)
                .build();
    }

    public BoardResponseDto toBoardResponseDto(Board board, List<Comment> comments) {
        if (board == null) {
            return null;
        }

        List<CommentResponseDto> commentDtos = null;

        if (comments != null && comments.size() > 0) {
            commentDtos = comments.stream()
                    .map(this::toCommentResponseDto).toList();
        }

        User user = userRepository.findByUserId(board.getBoardUserId());

        if (user == null) {
            throw new EntityNotFoundException("작성자를 알 수 없습니다.");
        }

        return BoardResponseDto.builder()
                .boardId(board.getBoardId())
                .userName(user.getUserName())
                .boardTitle(board.getBoardTitle())
                .boardContent(board.getBoardContent())
                .createdAt(board.getBoardCreatedAt())
                .modifiedAt(board.getBoardModifiedAt())
                .comments(commentDtos)
                .build();
    }

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
                .commentNum(commentCount)
                .build();
    }

    public CommentResponseDto toCommentResponseDto(Comment comment) {
        if (comment == null) {
            return null;
        }

        User user = userRepository.findByUserId(comment.getCommentUserId());

        if (user == null) {
            throw new EntityNotFoundException("작성자를 알 수 없습니다.");
        }

        List<CommentResponseDto> subComments = comment.getSubComments() != null
                ? comment.getSubComments().stream()
                .map(this::toCommentResponseDto).toList()
                : Collections.emptyList();

        return CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .userName(user.getUserName())
                .commentContent(comment.getCommentContent())
                .createdAt(comment.getCommentCreatedAt())
                .modifiedAt(comment.getCommentModifiedAt())
                .subComments(subComments)
                .build();
    }
}
