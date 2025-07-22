package com.antmen.antwork.domain.board.rule;






public interface BoardFetchStrategy {
    List<BoardListResponseDto> fetchBoards(String name, String sortBy);
}
