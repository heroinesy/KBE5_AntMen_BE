package com.antmen.antwork.domain.board.rule;

import com.antmen.antwork.common.api.response.board.BoardListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BoardStrategyFactory {
    private final Map<String, BoardFetchStrategy> strategyMap;

    public List<BoardListResponseDto> fetchBoards(String userType, String boardtype, String name, String sortBy) {
        String key = userType + "-" + boardtype;
        BoardFetchStrategy strategy = strategyMap.get(key);

        if (strategy == null) {
            throw new IllegalArgumentException("Invalid userType or boardType");
        }
        return strategy.fetchBoards(name, sortBy);
    }
}
