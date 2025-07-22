package com.antmen.antwork.domain.board.rule;








@Component("customer-personal")
@RequiredArgsConstructor
public class CustomerStrategy implements BoardFetchStrategy{

    private final BoardRepository boardRepository;

    @Override
    public List<BoardListResponseDto> fetchBoards(String name, String sortBy) {
        return boardRepository.searchPersonalBoardsWithPaging("customer", name, sortBy);
    }
}
