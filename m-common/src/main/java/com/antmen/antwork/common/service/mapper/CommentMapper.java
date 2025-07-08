package com.antmen.antwork.common.service.mapper;

import com.antmen.antwork.common.api.request.board.CommentRequestDto;
import com.antmen.antwork.common.api.response.board.CommentResponseDto;
import com.antmen.antwork.common.domain.entity.Board;
import com.antmen.antwork.common.domain.entity.Comment;
import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.infra.repository.board.CommentRepository;
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
                    .commentModifiedAt(LocalDateTime.now())
                    .commentParentId(commentRequestDto.getParentId())
                    .commentIsDeleted(false)
                    .build();
        }
    }

//    public CommentResponseDto toResponseDto(Comment comment) {
//        if (comment == null) {
//            return null;
//        }
//
//        return CommentResponseDto.builder()
//                .commentId(comment.getCommentId())
//                .userName(comment.getCommentUser() != null ? comment.getCommentUser().getUserName() : null)
//                .commentContent(comment.getCommentContent())
//                .createdAt(comment.getCommentCreatedAt())
//                .modifiedAt(comment.getCommentModifiedAt())
//                .build();
//    }
//}
