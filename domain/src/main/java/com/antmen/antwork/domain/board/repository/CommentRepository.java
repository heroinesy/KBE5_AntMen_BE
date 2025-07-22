package com.antmen.antwork.domain.board.repository;









@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoardId(Long boardId);

    @Query("SELECT c FROM Comment c WHERE c.boardId = :boardId AND c.commentParentId IS NULL AND c.commentIsDeleted = false ORDER BY c.commentCreatedAt ASC")
    List<Comment> findParentCommentsByBoardId(@Param("boardId") Long boardId);

    long countByBoardId(Long boardId);
}

