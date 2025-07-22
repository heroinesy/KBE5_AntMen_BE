package com.antmen.antwork.domain.board.rule;








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
