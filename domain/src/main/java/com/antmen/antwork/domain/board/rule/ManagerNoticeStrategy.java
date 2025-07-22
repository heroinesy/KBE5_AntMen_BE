package com.antmen.antwork.domain.board.rule;

import com.antmen.antwork.domain.board.dto.BoardListResponseDto;
import com.antmen.antwork.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("manager-notice")
@RequiredArgsConstructor
public class ManagerNoticeStrategy implements BoardFetchStrategy{
    private final BoardRepository boardRepository;

    @Override
    public List<BoardListResponseDto> fetchBoards(String name, String sortBy) {
        return boardRepository.searchManagerNoticesWithPaging(name, sortBy);
    }
}
