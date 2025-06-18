package com.antmen.antwork.common.infra.repository.board;

import com.antmen.antwork.common.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
    List<Board> findAllByBoardTypeAndIsPinned(String boardType, boolean isPinned);
    List<Board> findAllByBoardUserIdAndIsPinned(Long boardUserId, boolean isPinned);
}
