package com.antmen.antwork.common.service.strategy;

import com.antmen.antwork.common.api.response.board.BoardListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class BoardStrategyFactory {
    private final Map<String, BoardFetchStrategy> strategyMap;

    public Page<BoardListResponseDto> fetchBoards(String userType, String boardtype, String name, String sortBy, String filter, Pageable pageable) {
        String key = userType + "-" + boardtype;
        BoardFetchStrategy strategy = strategyMap.get(key);

        if (strategy == null) {
            throw new IllegalArgumentException("Invalid userType or boardType");
        }
        return strategy.fetchBoards(name, sortBy, filter, pageable);
    }
}
