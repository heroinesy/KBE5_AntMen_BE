package com.antmen.antwork.domain.board.rule;

import com.antmen.antwork.domain.board.dto.BoardListResponseDto;
import com.antmen.antwork.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("manager-personal")
@RequiredArgsConstructor
public class ManagerStrategy implements BoardFetchStrategy{

    private final BoardRepository boardRepository;

    @Override
    public List<BoardListResponseDto> fetchBoards(String name, String sortBy) {
        return boardRepository.searchPersonalBoardsWithPaging("manager", name, sortBy);
    }
}
