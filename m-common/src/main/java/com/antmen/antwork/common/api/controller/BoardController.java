package com.antmen.antwork.common.api.controller;

import com.antmen.antwork.common.api.request.board.BoardRequestDto;
import com.antmen.antwork.common.api.request.board.CommentRequestDto;
import com.antmen.antwork.common.api.response.board.BoardListResponseDto;
import com.antmen.antwork.common.api.response.board.BoardResponseDto;
import com.antmen.antwork.common.service.BoardService;
import com.antmen.antwork.common.service.CommentService;
import com.antmen.antwork.common.util.AuthUserDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
public class BoardController {

    public final BoardService boardService;
    public final CommentService commentService;

//    @PostMapping("/{boardType}")
//    public ResponseEntity boardWrite(@AuthenticationPrincipal AuthUserDto authUserDto, @PathVariable String boardType, @RequestBody BoardRequestDto boardRequestDto) {
//
//        log.info("request DTO : {}", boardRequestDto);
//        Long userId = authUserDto.getUserIdAsLong();
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(boardService.boardWrite(boardType, boardRequestDto, userId));
//    }

    @GetMapping("/{boardType}")
    public ResponseEntity<Map<String, List<BoardListResponseDto>>> boardReadList(
            @PathVariable String boardType,
            @AuthenticationPrincipal AuthUserDto authUserDto) {
        return ResponseEntity.status(HttpStatus.OK).body(boardService.boardReadList(boardType, authUserDto.getUserIdAsLong()));
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<BoardResponseDto> boardRead(@PathVariable Long id) {
//        return ResponseEntity.status(HttpStatus.FOUND).body(boardService.boardRead(id));
//    }

//    @PutMapping("/{id}")
//    public ResponseEntity<BoardResponseDto> boardUpdate(@AuthenticationPrincipal AuthUserDto authUserDto, BoardRequestDto boardRequestDto, @PathVariable Long id) {
//
//        Long userId = authUserDto.getUserIdAsLong();
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(boardService.boardUpdate(userId, id, boardRequestDto));
//    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity boardDelete(@AuthenticationPrincipal AuthUserDto authUserDto, @PathVariable Long id) {
//
//        Long userId = authUserDto.getUserIdAsLong();
//        boardService.deleteBoard(userId, id);
//        return ResponseEntity
//                .status(HttpStatus.NO_CONTENT)
//                .build();
//    }

//    @PostMapping("/comment/{boardId}")
//    public ResponseEntity commentWrite(@AuthenticationPrincipal AuthUserDto authUserDto, @PathVariable Long boardId, CommentRequestDto commentRequestDto) {
//
//        Long userId = authUserDto.getUserIdAsLong();
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(commentService.commentWrite(userId, boardId, commentRequestDto));
//    }

//    @PutMapping("/{boardId}/{commentId}")
//    public ResponseEntity commentUpdate(@AuthenticationPrincipal AuthUserDto authUserDto, @PathVariable Long commentId, CommentRequestDto commentRequestDto) {
//
//        Long userId = authUserDto.getUserIdAsLong();
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(commentService.commentUpdate(userId, commentId, commentRequestDto));
//    }

//    @DeleteMapping("/{boardId}/{commentId}")
//    public ResponseEntity commentDelete(@AuthenticationPrincipal AuthUserDto authUserDto, @PathVariable Long commentId, CommentRequestDto commentRequestDto) {
//
//        Long userId = authUserDto.getUserIdAsLong();
//        commentService.commentDelete(userId, commentId);
//        return ResponseEntity
//                .status(HttpStatus.NO_CONTENT)
//                .build();
//    }

//    @PostMapping("/{boardId}/{commentId}")
//    public ResponseEntity subcommentWrite(@AuthenticationPrincipal AuthUserDto authUserDto, @PathVariable Long boardId, @PathVariable Long commentId, CommentRequestDto commentRequestDto) {
//
//        Long userId = authUserDto.getUserIdAsLong();
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(commentService.subCommentWrite(userId, boardId, commentId,commentRequestDto));
//    }


}
