package com.antmen.antwork.domain.board.repository;


import com.antmen.antwork.domain.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoardId(Long boardId);

    @Query("SELECT c FROM Comment c WHERE c.boardId = :boardId AND c.commentParentId IS NULL AND c.commentIsDeleted = false ORDER BY c.commentCreatedAt ASC")
    List<Comment> findParentCommentsByBoardId(@Param("boardId") Long boardId);

    long countByBoardId(Long boardId);
}