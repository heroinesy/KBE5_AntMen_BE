package com.antmen.antwork.domain.board.rule;








@Component("customer-notice")
@RequiredArgsConstructor
public class CustomerNoticeStrategy implements BoardFetchStrategy{
    private final BoardRepository boardRepository;

    @Override
    public List<BoardListResponseDto> fetchBoards(String name, String sortBy) {
        return boardRepository.searchCustomerNoticesWithPaging(name, sortBy);
    }
}
