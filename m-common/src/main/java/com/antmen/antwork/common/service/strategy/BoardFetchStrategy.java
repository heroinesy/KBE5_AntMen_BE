package com.antmen.antwork.common.service.strategy;

import com.antmen.antwork.common.api.response.board.BoardListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BoardFetchStrategy {
    Page<BoardListResponseDto> fetchBoards(String name, String sortBy, String filter, Pageable pageable);
}
