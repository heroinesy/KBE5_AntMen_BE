package com.antmen.antwork.domain.board.rule;

import com.antmen.antwork.domain.board.dto.BoardListResponseDto;

import java.util.List;

public interface BoardFetchStrategy {
    List<BoardListResponseDto> fetchBoards(String name, String sortBy);
}
