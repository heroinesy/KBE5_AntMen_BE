package com.antmen.antwork.domain.board.service;


import com.antmen.antwork.domain.board.dto.BoardListResponseDto;
import com.antmen.antwork.domain.board.dto.BoardRequestDto;
import com.antmen.antwork.domain.board.dto.BoardResponseDto;
import com.antmen.antwork.domain.board.dto.PostPageDto;
import com.antmen.antwork.domain.board.entity.Board;
import com.antmen.antwork.domain.board.entity.BoardStatus;
import com.antmen.antwork.domain.board.entity.Comment;
import com.antmen.antwork.domain.board.mapper.BoardMapper;
import com.antmen.antwork.domain.board.repository.BoardRepository;
import com.antmen.antwork.domain.board.repository.CommentRepository;
import com.antmen.antwork.domain.board.rule.BoardStrategyFactory;
import com.antmen.antwork.domain.user.repository.UserRepository;
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

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardStrategyFactory boardStrategyFactory;

    @Transactional
    public void boardWrite(String boardType, BoardRequestDto boardRequestDto, Long userId) {

        if ((boardType.equals("customer") || boardType.equals("manager")) && (boardRequestDto.getBoardIsPinned() == false)) {
            boardRequestDto.setBoardStatus(BoardStatus.New);
        }

        if (boardRequestDto.getBoardReservatedAt() != null) {
            boardRequestDto.setBoardStatus(BoardStatus.Reserved);
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

//        if (board.getBoardIsDeleted()){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "삭제된 게시글 입니다.");
//        }

        List<Comment> parentComments = commentRepository.findParentCommentsByBoardId(boardId);


        return boardMapper.toBoardResponseDto(board, parentComments);
    }

    @Transactional(readOnly = true)
    public Object getBoardAdminList(String usertype, String boardType, String name, String sortBy) {
        return boardStrategyFactory.fetchBoards(usertype, boardType, name, sortBy);
    }

    @Transactional
    public BoardResponseDto boardUpdate(Long userId, Long boardId, BoardRequestDto boardRequestDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        if (board.getBoardIsDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "삭제된 게시글 입니다.");
        }

        if (!board.getBoardUserId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인이 작성한 글만 수정 가능합니다.");
        }

        board.setBoardTitle(boardRequestDto.getBoardTitle());
        board.setBoardContent(boardRequestDto.getBoardContent());
        board.setIsPinned(boardRequestDto.getBoardIsPinned());
        board.setBoardReservedAt(boardRequestDto.getBoardReservatedAt());
        board.setBoardModifiedAt(LocalDateTime.now());

        return boardRead(boardId);
    }

    @Transactional
    public void deleteBoard(Long userId, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        if (board.getBoardIsDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "이미 삭제된 게시글 입니다.");
        }

        if (!board.getBoardUserId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인이 작성한 글만 삭제 가능합니다.");
        }

        board.setBoardIsDeleted(true);
    }

    @Transactional
    public void boardResolved(Long userId, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        if (board.getBoardIsDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "이미 삭제된 게시글 입니다.");
        }

        if (!board.getBoardUserId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "문의 완료 여부는 작성자만 결정할 수 있습니다.");
        }

        board.setBoardStatus(BoardStatus.Resolved);
    }
}