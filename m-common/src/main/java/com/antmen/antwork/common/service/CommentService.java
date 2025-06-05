package com.antmen.antwork.common.service;

import com.antmen.antwork.common.api.request.board.CommentRequestDto;
import com.antmen.antwork.common.api.response.board.CommentResponseDto;
import com.antmen.antwork.common.domain.entity.Board;
import com.antmen.antwork.common.domain.entity.Comment;
import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.infra.repository.board.BoardRepository;
import com.antmen.antwork.common.infra.repository.board.CommentRepository;
import com.antmen.antwork.common.infra.repository.account.UserRepository;
import com.antmen.antwork.common.service.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public CommentResponseDto commentWrite(Long userId, Long boardId, CommentRequestDto commentRequestDto) {
        User user = userRepository.findById(userId).get();
        Board board = boardRepository.findById(boardId).get();
        Comment newComment = commentRepository.save(commentMapper.toEntity(board, user, commentRequestDto));
        return commentMapper.toResponseDto(newComment);
    }

    public CommentResponseDto commentUpdate(Long userId, Long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."));

        if (comment.getCommentIsDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "삭제된 댓글입니다.");
        }

        if (comment.getCommentUser().getUserId() != userId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인이 작성한 댓글만 수정 가능합니다.");
        }

        comment.setCommentContent(commentRequestDto.getContent());

        return commentMapper.toResponseDto(comment);
    }

    public void commentDelete(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."));

        if (comment.getCommentIsDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "이미 삭제된 댓글입니다.");
        }

        if (comment.getCommentUser().getUserId() != userId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인이 작성한 댓글만 삭제 가능합니다.");
        }

        comment.setCommentIsDeleted(true);
    }

    public CommentResponseDto subCommentWrite(Long userId, Long boardId, Long commentId, CommentRequestDto commentRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
        
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
        
        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "부모 댓글을 찾을 수 없습니다."));
        
        // 부모 댓글과 게시글이 일치하는지 확인
        if (!parentComment.getBoard().getBoardId().equals(boardId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글이 해당 게시글에 속하지 않습니다.");
        }
        
        Comment newComment = commentRepository.save(commentMapper.toEntity(board, user, parentComment, commentRequestDto));
        return commentMapper.toResponseDto(newComment);
    }
}