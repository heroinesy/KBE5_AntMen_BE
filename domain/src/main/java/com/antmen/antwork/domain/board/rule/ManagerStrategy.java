package com.antmen.antwork.domain.board.rule;








@Component("manager-personal")
@RequiredArgsConstructor
public class ManagerStrategy implements BoardFetchStrategy{

    private final BoardRepository boardRepository;

    @Override
    public List<BoardListResponseDto> fetchBoards(String name, String sortBy) {
        return boardRepository.searchPersonalBoardsWithPaging("manager", name, sortBy);
    }
}
