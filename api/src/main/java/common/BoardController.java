package common;

import com.antmen.antwork.common.api.request.board.BoardRequestDto;
import com.antmen.antwork.common.api.request.board.CommentRequestDto;
import com.antmen.antwork.common.api.response.board.BoardListResponseDto;
import com.antmen.antwork.common.api.response.board.BoardResponseDto;
import com.antmen.antwork.common.api.response.board.PostPageDto;
import com.antmen.antwork.common.service.BoardService;
import com.antmen.antwork.common.service.CommentService;
import com.antmen.antwork.common.util.AuthUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
public class BoardController {

    public final BoardService boardService;
    public final CommentService commentService;

    @PostMapping("/{boardType}")
    public ResponseEntity boardWrite(@AuthenticationPrincipal AuthUserDto authUserDto, @PathVariable String boardType, @RequestBody BoardRequestDto boardRequestDto) {

        log.info("request DTO : {}", boardRequestDto);
        Long userId = authUserDto.getUserIdAsLong();
        boardService.boardWrite(boardType, boardRequestDto, userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/{boardType}/list")
    public ResponseEntity<PostPageDto> boardReadList(
            @PathVariable String boardType,
            @AuthenticationPrincipal AuthUserDto authUserDto,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sortBy,
            @PageableDefault(size = 5) Pageable pageable
            ) {

        Long userId = 0L;
        if (authUserDto != null) {
            userId = authUserDto.getUserIdAsLong();
        }
        return ResponseEntity.status(HttpStatus.OK).body(boardService.boardReadList(boardType, userId, name, sortBy, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> boardRead(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(boardService.boardRead(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardResponseDto> boardUpdate(@AuthenticationPrincipal AuthUserDto authUserDto, @RequestBody BoardRequestDto boardRequestDto, @PathVariable Long id) {

        Long userId = authUserDto.getUserIdAsLong();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(boardService.boardUpdate(userId, id, boardRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity boardDelete(@AuthenticationPrincipal AuthUserDto authUserDto, @PathVariable Long id) {

        Long userId = authUserDto.getUserIdAsLong();
        boardService.deleteBoard(userId, id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PostMapping("/comment/{boardId}")
    public ResponseEntity commentWrite(@AuthenticationPrincipal AuthUserDto authUserDto, @PathVariable Long boardId, @RequestBody CommentRequestDto commentRequestDto) {

        Long userId = authUserDto.getUserIdAsLong();
        commentService.commentWrite(userId, boardId, commentRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PutMapping("/{boardId}/{commentId}")
    public ResponseEntity commentUpdate(@AuthenticationPrincipal AuthUserDto authUserDto, @PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto) {

        Long userId = authUserDto.getUserIdAsLong();
        commentService.commentUpdate(userId, commentId, commentRequestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/{boardId}/{commentId}")
    public ResponseEntity commentDelete(@AuthenticationPrincipal AuthUserDto authUserDto, @PathVariable Long commentId) {

        Long userId = authUserDto.getUserIdAsLong();
        commentService.commentDelete(userId, commentId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping("/{boardId}/resolved")
    public ResponseEntity boardResolved(@AuthenticationPrincipal AuthUserDto authUserDto, @PathVariable Long boardId) {
        Long userId = authUserDto.getUserIdAsLong();
        boardService.boardResolved(userId, boardId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

//    @PostMapping("/{boardId}/{commentId}")
//    public ResponseEntity subcommentWrite(@AuthenticationPrincipal AuthUserDto authUserDto, @PathVariable Long boardId, @PathVariable Long commentId, CommentRequestDto commentRequestDto) {
//
//        Long userId = authUserDto.getUserIdAsLong();
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(commentService.subCommentWrite(userId, boardId, commentId,commentRequestDto));
//    }


}
