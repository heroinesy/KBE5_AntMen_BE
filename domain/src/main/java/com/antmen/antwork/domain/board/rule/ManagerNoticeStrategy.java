package com.antmen.antwork.domain.board.rule;








@Component("manager-notice")
@RequiredArgsConstructor
public class ManagerNoticeStrategy implements BoardFetchStrategy{
    private final BoardRepository boardRepository;

    @Override
    public List<BoardListResponseDto> fetchBoards(String name, String sortBy) {
        return boardRepository.searchManagerNoticesWithPaging(name, sortBy);
    }
}
