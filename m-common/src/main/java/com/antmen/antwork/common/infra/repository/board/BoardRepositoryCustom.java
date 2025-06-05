package com.antmen.antwork.common.infra.repository.board;

import com.antmen.antwork.common.domain.entity.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepositoryCustom {
    Optional<Board> findByIdWithCommentsAndSubComments(Long boardId);
    List<Board> findAllByBoardType(String boardType);
}
