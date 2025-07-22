package com.antmen.antwork.domain.board.rule;

import com.antmen.antwork.domain.board.dto.BoardListResponseDto;
import com.antmen.antwork.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("customer-personal")
@RequiredArgsConstructor
public class CustomerStrategy implements BoardFetchStrategy{

    private final BoardRepository boardRepository;

    @Override
    public List<BoardListResponseDto> fetchBoards(String name, String sortBy) {
        return boardRepository.searchPersonalBoardsWithPaging("customer", name, sortBy);
    }
}
