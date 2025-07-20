package com.antmen.antwork.common.service;

import com.antmen.antwork.common.api.request.board.CommentRequestDto;
import com.antmen.antwork.common.domain.entity.AlertTrigger;
import com.antmen.antwork.common.domain.entity.Board;
import com.antmen.antwork.common.domain.entity.BoardStatus;
import com.antmen.antwork.common.domain.entity.Comment;
import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.domain.entity.account.UserRole;
import com.antmen.antwork.common.infra.repository.board.BoardRepository;
import com.antmen.antwork.common.infra.repository.board.CommentRepository;
import com.antmen.antwork.common.infra.repository.account.UserRepository;
import com.antmen.antwork.common.service.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final AlertService alertService;

    @Transactional
    public void commentWrite(Long userId, Long boardId, CommentRequestDto commentRequestDto) {
        User user = userRepository.findByUserId(userId);
        Board board = boardRepository.findById(boardId).get();
        if (board.getBoardIsDeleted() == true) {
            throw new IllegalArgumentException("삭제된 게시글 입니다.");
        }
        commentRepository.save(commentMapper.toEntity(userId, boardId, commentRequestDto));

        if ((board.getBoardType().equals("customer") || board.getBoardType().equals("manager"))
                && board.getIsPinned() == false && user.getUserRole() == UserRole.ADMIN) {
            board.setBoardStatus(BoardStatus.InProgress);

            if(board.getBoardType().equals("customer")) {
                alertService.sendAlert(board.getBoardUserId(), AlertTrigger.COMMENT_ON_POST_FOR_CUSTOMER,boardId);
            } else {
                alertService.sendAlert(board.getBoardUserId(), AlertTrigger.COMMENT_ON_POST_FOR_MANAGER,boardId);
            }

        }

    }

    @Transactional
    public void commentUpdate(Long userId, Long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."));

        if (comment.getCommentIsDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "삭제된 댓글입니다.");
        }

        if (!comment.getCommentUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인이 작성한 댓글만 수정 가능합니다.");
        }

        comment.setCommentContent(commentRequestDto.getContent());
        comment.setCommentModifiedAt(LocalDateTime.now());
    }

    @Transactional
    public void commentDelete(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."));

        if (comment.getCommentIsDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "이미 삭제된 댓글입니다.");
        }

        User user = userRepository.findByUserId(userId);

        if (!comment.getCommentUserId().equals(userId)) {
            if (user.getUserRole() == UserRole.ADMIN) {
                comment.setCommentContent("관리자가 삭제한 댓글입니다.");
                comment.setCommentModifiedAt(LocalDateTime.now());
                return;
            }
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인이 작성한 댓글만 삭제 가능합니다.");
        }

        comment.setCommentIsDeleted(true);
    }

//    public CommentResponseDto subCommentWrite(Long userId, Long boardId, Long commentId, CommentRequestDto commentRequestDto) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
//
//        Board board = boardRepository.findById(boardId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
//
//        Comment parentComment = commentRepository.findById(commentId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "부모 댓글을 찾을 수 없습니다."));
//
//        // 부모 댓글과 게시글이 일치하는지 확인
//        if (!parentComment.getBoard().getBoardId().equals(boardId)) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글이 해당 게시글에 속하지 않습니다.");
//        }
//
//        Comment newComment = commentRepository.save(commentMapper.toEntity(board, user, parentComment, commentRequestDto));
//        return commentMapper.toResponseDto(newComment);
//    }
}