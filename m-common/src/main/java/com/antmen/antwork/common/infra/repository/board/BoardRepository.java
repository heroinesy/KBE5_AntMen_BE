package com.antmen.antwork.common.infra.repository.board;

import com.antmen.antwork.common.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
    Optional<Board> findByBoardId(Long boardId);
//    List<Board> findAllByBoardTypeAndIsPinnedIsTrue(String boardType);


}
