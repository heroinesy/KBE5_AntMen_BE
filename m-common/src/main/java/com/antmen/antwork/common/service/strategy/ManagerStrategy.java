package com.antmen.antwork.common.service.strategy;

import com.antmen.antwork.common.api.response.board.BoardListResponseDto;
import com.antmen.antwork.common.infra.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component("manager-personal")
@RequiredArgsConstructor
public class ManagerStrategy implements BoardFetchStrategy{

    private final BoardRepository boardRepository;

    @Override
    public Page<BoardListResponseDto> fetchBoards(String name, String sortBy, String filter, Pageable pageable) {
        return boardRepository.searchPersonalBoardsWithPaging("manager", name, sortBy, filter, pageable);
    }
}
