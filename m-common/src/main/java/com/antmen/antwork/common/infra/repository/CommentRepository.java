package com.antmen.antwork.common.infra.repository;

import com.antmen.antwork.common.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoardBoardId(Long boardId);
}

