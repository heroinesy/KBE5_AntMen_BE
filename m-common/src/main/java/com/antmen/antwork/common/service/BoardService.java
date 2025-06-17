package com.antmen.antwork.common.service;

import com.antmen.antwork.common.api.request.board.BoardRequestDto;
import com.antmen.antwork.common.api.response.board.BoardListResponseDto;
import com.antmen.antwork.common.api.response.board.BoardResponseDto;
import com.antmen.antwork.common.domain.entity.Board;
import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.infra.repository.board.BoardRepository;
import com.antmen.antwork.common.infra.repository.account.UserRepository;
import com.antmen.antwork.common.infra.repository.board.CommentRepository;
import com.antmen.antwork.common.service.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
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

//    @Transactional
//    public BoardResponseDto boardWrite(String boardType, BoardRequestDto boardRequestDto, Long userId) {
//        Board newBoard = boardMapper.toEntity(boardRequestDto, boardType, userId);
//        return boardMapper.toResponseDto(boardRepository.save(newBoard));
//    }

    @Transactional(readOnly = true)
    public Map<String, List<BoardListResponseDto>> boardReadList(String boardType, Long userId) {
        List<BoardListResponseDto> pinned = new ArrayList<>();
        List<BoardListResponseDto> normal = new ArrayList<>();

        List<Board> pinnedEn = new ArrayList<>();
        List<Board> normalEn = new ArrayList<>();

        if (boardType.endsWith("Personal")){
            pinnedEn = boardRepository.findAllByBoardUserIdAndIsPinned(userId, true);
            normalEn = boardRepository.findAllByBoardUserIdAndIsPinned(userId, false);
        } else {
            pinnedEn = boardRepository.findAllByBoardTypeAndIsPinned(boardType,true);
            normalEn = boardRepository.findAllByBoardTypeAndIsPinned(boardType,false);
        }

        for (Board board : pinnedEn) {
            Long commentCount = commentRepository.countByBoardId(board.getBoardId());
            String userName = userRepository.findById(board.getBoardUserId()).get().getUserName();
            pinned.add(boardMapper.toListResponseDto(board, commentCount, userName));
        }

        for (Board board : normalEn) {
            Long commentCount = commentRepository.countByBoardId(board.getBoardId());
            String userName = userRepository.findById(board.getBoardUserId()).get().getUserName();
            normal.add(boardMapper.toListResponseDto(board, commentCount, userName));
        }
        return Map.of("pinned", pinned, "nomal", normal);
    }

//    @Transactional(readOnly = true)
//    public BoardResponseDto boardRead(Long boardId) {
//        // 게시글과 댓글, 대댓글을 함께 조회 (QueryDSL 사용)
//        Board board = boardRepository.findByIdWithCommentsAndSubComments(boardId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
//
//        if (board.getBoardIsDeleted()){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "삭제된 게시글 입니다.");
//        }
//
//        return boardMapper.toResponseDto(board);
//    }

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
