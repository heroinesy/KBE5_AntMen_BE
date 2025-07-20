package com.antmen.antwork.domain.board.repository;

import com.antmen.antwork.common.api.response.board.BoardListResponseDto;
import com.antmen.antwork.common.domain.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BoardRepositoryCustom {
    Optional<Board> findByIdWithCommentsAndSubComments(Long boardId);
    List<Board> findAllByBoardType(String boardType);

    List<BoardListResponseDto> findAllByBoardTypeAndIsPinnedIsTrue(String boardType);
    Page<BoardListResponseDto> searchBoardsWithPaging(String boardType, Long userId, String name, String sortBy, Pageable pageable);

    List<BoardListResponseDto> searchCustomerNoticesWithPaging(String name, String sortBy);
    List<BoardListResponseDto> searchManagerNoticesWithPaging(String name, String sortBy);
    List<BoardListResponseDto> searchPersonalBoardsWithPaging(String boardType, String name, String sortBy);


}
