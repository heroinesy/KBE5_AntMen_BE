package com.antmen.antwork.domain.board.rule;

import com.antmen.antwork.common.api.response.board.BoardListResponseDto;
import com.antmen.antwork.common.infra.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("customer-notice")
@RequiredArgsConstructor
public class CustomerNoticeStrategy implements BoardFetchStrategy{
    private final BoardRepository boardRepository;

    @Override
    public List<BoardListResponseDto> fetchBoards(String name, String sortBy) {
        return boardRepository.searchCustomerNoticesWithPaging(name, sortBy);
    }
}
