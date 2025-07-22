package com.antmen.antwork.domain.board.mapper;

import com.antmen.antwork.domain.board.dto.CommentRequestDto;
import com.antmen.antwork.domain.board.entity.Comment;
import com.antmen.antwork.domain.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final CommentRepository commentRepository;

    public Comment toEntity(Long userId, Long boardId, CommentRequestDto commentRequestDto) {
            return Comment.builder()
                    .commentUserId(userId)
                    .boardId(boardId)
                    .commentContent(commentRequestDto.getContent())
                    .commentCreatedAt(LocalDateTime.now())
                    .commentModifiedAt(null)
                    .commentParentId(commentRequestDto.getParentId())
                    .commentIsDeleted(false)
                    .build();
        }
    }