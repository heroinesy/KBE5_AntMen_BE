package com.antmen.antwork.common.api.controller;

import com.antmen.antwork.common.api.request.board.BoardRequestDto;
import com.antmen.antwork.common.api.request.board.CommentRequestDto;
import com.antmen.antwork.common.api.response.board.BoardListResponseDto;
import com.antmen.antwork.common.api.response.board.BoardResponseDto;
import com.antmen.antwork.common.service.BoardService;
import com.antmen.antwork.common.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
public class BoardController {

    public final BoardService boardService;
    public final CommentService commentService;

    @PostMapping("/{boardType}")
    public ResponseEntity boardWrite(HttpServletRequest request, @PathVariable String boardType, @RequestBody BoardRequestDto boardRequestDto) {
//        Long userId = (Long) request.getSession().getAttribute("userId");
        log.info("request DTO : {}", boardRequestDto);
        Long userId = 1L;
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(boardService.boardWrite(boardType, boardRequestDto, userId));
    }

    @GetMapping("/{boardType}")
    public ResponseEntity<List<BoardListResponseDto>> boardReadList(@PathVariable String boardType) {
        return ResponseEntity.status(HttpStatus.OK).body(boardService.boardReadList(boardType));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> boardRead(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(boardService.boardRead(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardResponseDto> boardUpdate(HttpServletRequest request, BoardRequestDto boardRequestDto, @PathVariable Long id) {
//        Long userId = (Long) request.getSession().getAttribute("userId");
        Long userId = 1L;
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(boardService.boardUpdate(userId, id, boardRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity boardDelete(HttpServletRequest request, @PathVariable Long id) {
//        Long userId = (Long) request.getSession().getAttribute("userId");
        Long userId = 1L;
        boardService.deleteBoard(userId, id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PostMapping("/comment/{boardId}")
    public ResponseEntity commentWrite(HttpServletRequest request, @PathVariable Long boardId, CommentRequestDto commentRequestDto) {
//        Long userId = (Long) request.getSession().getAttribute("userId");
        Long userId = 1L;
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.commentWrite(userId, boardId, commentRequestDto));
    }

    @PutMapping("/{boardId}/{commentId}")
    public ResponseEntity commentUpdate(HttpServletRequest request, @PathVariable Long commentId, CommentRequestDto commentRequestDto) {
//        Long userId = (Long) request.getSession().getAttribute("userId");
        Long userId = 1L;
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.commentUpdate(userId, commentId, commentRequestDto));
    }

    @DeleteMapping("/{boardId}/{commentId}")
    public ResponseEntity commentDelete(HttpServletRequest request, @PathVariable Long commentId, CommentRequestDto commentRequestDto) {
//        Long userId = (Long) request.getSession().getAttribute("userId");
        Long userId = 1L;
        commentService.commentDelete(userId, commentId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PostMapping("/{boardId}/{commentId}")
    public ResponseEntity subcommentWrite(HttpServletRequest request, @PathVariable Long boardId, @PathVariable Long commentId, CommentRequestDto commentRequestDto) {
//        Long userId = (Long) request.getSession().getAttribute("userId");
        Long userId = 1L;
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.subCommentWrite(userId, boardId, commentId,commentRequestDto));
    }


}
