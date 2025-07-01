package com.antmen.antwork.common.service;

import com.antmen.antwork.common.api.request.board.BoardRequestDto;
import com.antmen.antwork.common.api.response.board.BoardListResponseDto;
import com.antmen.antwork.common.api.response.board.BoardResponseDto;
import com.antmen.antwork.common.api.response.board.PostPageDto;
import com.antmen.antwork.common.domain.entity.Board;
import com.antmen.antwork.common.domain.entity.Comment;
import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.infra.repository.board.BoardRepository;
import com.antmen.antwork.common.infra.repository.account.UserRepository;
import com.antmen.antwork.common.infra.repository.board.CommentRepository;
import com.antmen.antwork.common.service.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public void boardWrite(String boardType, BoardRequestDto boardRequestDto, Long userId) {

        switch (boardType) {
            case "costumer-notice":
                boardType = "customerNotice";
                break;
        }

        Board newBoard = boardMapper.toEntity(boardRequestDto, boardType, userId);
        boardRepository.save(newBoard);
    }

    @Transactional(readOnly = true)
    public PostPageDto boardReadList(String boardType, Long userId, String name, String sortby, Pageable pageable) {
        List<BoardListResponseDto> pinned = new ArrayList<>();
        Page<BoardListResponseDto> normal = null;

        pinned = boardRepository.findAllByBoardTypeAndIsPinnedIsTrue(boardType);

        if (boardType.endsWith("notice")) {
            normal = boardRepository.searchBoardsWithPaging(boardType, null, name, sortby, pageable);
        } else {
            normal = boardRepository.searchBoardsWithPaging(boardType, userId, name, sortby, pageable);
        }

        return PostPageDto.builder()
                .pinnedPosts(pinned)
                .posts(normal)
                .build();

    }

    @Transactional(readOnly = true)
    public BoardResponseDto boardRead(Long boardId) {

        Board board = boardRepository.findByBoardId(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        if (board.getBoardIsDeleted()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "삭제된 게시글 입니다.");
        }

        List<Comment> comments = commentRepository.findParentCommentsByBoardId(boardId);

        return boardMapper.toBoardResponseDto(board, comments);
    }

//    @Transactional
//    public BoardResponseDto boardUpdate(Long userId, Long boardId, BoardRequestDto boardRequestDto) {
//        Board board = boardRepository.findById(boardId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
//
//        if (board.getBoardIsDeleted()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "삭제된 게시글 입니다.");
//        }
//
//        if (board.getBoardUser().getUserId() != userId){
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인이 작성한 글만 수정 가능합니다.");
//        }
//
//        board.setBoardTitle(boardRequestDto.getBoardTitle());
//        board.setBoardContent(boardRequestDto.getBoardContent());
//        board.setIsPinned(boardRequestDto.getBoardIsPinned());
//        board.setBoardModifiedAt(LocalDateTime.now());
//
//        return boardMapper.toResponseDto(board);
//    }

//    @Transactional
//    public void deleteBoard(Long boardId, Long userId) {
//        Board board = boardRepository.findById(boardId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
//
//        if (board.getBoardIsDeleted()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "이미 삭제된 게시글 입니다.");
//        }
//
//        if (board.getBoardUser().getUserId() != userId){
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인이 작성한 글만 삭제 가능합니다.");
//        }
//
//        board.setBoardIsDeleted(true);
//    }

}
