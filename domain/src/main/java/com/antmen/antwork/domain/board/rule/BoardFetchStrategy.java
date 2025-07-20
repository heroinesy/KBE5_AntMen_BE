package com.antmen.antwork.domain.board.rule;

import com.antmen.antwork.common.api.response.board.BoardListResponseDto;

import java.util.List;


public interface BoardFetchStrategy {
    List<BoardListResponseDto> fetchBoards(String name, String sortBy);
}
