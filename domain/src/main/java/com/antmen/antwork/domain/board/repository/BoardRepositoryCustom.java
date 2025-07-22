package com.antmen.antwork.domain.board.repository;









public interface BoardRepositoryCustom {
    Optional<Board> findByIdWithCommentsAndSubComments(Long boardId);
    List<Board> findAllByBoardType(String boardType);

    List<BoardListResponseDto> findAllByBoardTypeAndIsPinnedIsTrue(String boardType);
    Page<BoardListResponseDto> searchBoardsWithPaging(String boardType, Long userId, String name, String sortBy, Pageable pageable);

    List<BoardListResponseDto> searchCustomerNoticesWithPaging(String name, String sortBy);
    List<BoardListResponseDto> searchManagerNoticesWithPaging(String name, String sortBy);
    List<BoardListResponseDto> searchPersonalBoardsWithPaging(String boardType, String name, String sortBy);


}
