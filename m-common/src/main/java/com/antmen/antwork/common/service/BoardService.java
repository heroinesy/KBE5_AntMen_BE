package com.antmen.antwork.common.service;

import com.antmen.antwork.common.api.request.BoardRequestDto;
import com.antmen.antwork.common.api.response.BoardListResponseDto;
import com.antmen.antwork.common.api.response.BoardResponseDto;
import com.antmen.antwork.common.domain.entity.Board;
import com.antmen.antwork.common.domain.entity.User;
import com.antmen.antwork.common.infra.repository.BoardRepository;
import com.antmen.antwork.common.infra.repository.UserRepository;
import com.antmen.antwork.common.service.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;
    private final UserRepository userRepository;

    @Transactional
    public BoardResponseDto boardWrite(String boardType, BoardRequestDto boardRequestDto, Long userId) {
        User user = userRepository.findById(userId).get();
        Board newBoard = boardMapper.toEntity(boardRequestDto, boardType, user);
//        newBoard.setBoardCreatedAt(LocalDateTime.now());
//        newBoard.setBoardModifiedAt(LocalDateTime.now());
        return boardMapper.toResponseDto(boardRepository.save(newBoard));
    }

    @Transactional(readOnly = true)
    public List<BoardListResponseDto> boardReadList(String boardType) {
        return boardRepository.findAllByBoardType(boardType)
                .stream().map(boardMapper::toListResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public BoardResponseDto boardRead(Long boardId) {
        // 게시글과 댓글, 대댓글을 함께 조회 (QueryDSL 사용)
        Board board = boardRepository.findByIdWithCommentsAndSubComments(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        if (board.getBoardIsDeleted()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "삭제된 게시글 입니다.");
        }

        return boardMapper.toResponseDto(board);
    }

    @Transactional
    public BoardResponseDto boardUpdate(Long userId, Long boardId, BoardRequestDto boardRequestDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        if (board.getBoardIsDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "삭제된 게시글 입니다.");
        }

        if (board.getBoardUser().getUserId() != userId){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인이 작성한 글만 수정 가능합니다.");
        }

        board.setBoardTitle(boardRequestDto.getBoardTitle());
        board.setBoardContent(boardRequestDto.getBoardContent());
        board.setIsPinned(boardRequestDto.getBoardIsPinned());
        board.setBoardModifiedAt(LocalDateTime.now());

        return boardMapper.toResponseDto(board);
    }

    @Transactional
    public void deleteBoard(Long boardId, Long userId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        if (board.getBoardIsDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "이미 삭제된 게시글 입니다.");
        }

        if (board.getBoardUser().getUserId() != userId){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인이 작성한 글만 삭제 가능합니다.");
        }

        board.setBoardIsDeleted(true);
    }

}
