package com.antmen.antwork.common.infra.repository.board;

import com.antmen.antwork.common.domain.entity.Board;
import com.antmen.antwork.common.domain.entity.QBoard;
import com.antmen.antwork.common.domain.entity.QComment;
import com.antmen.antwork.common.domain.entity.account.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Board> findByIdWithCommentsAndSubComments(Long boardId) {
        QBoard qBoard = QBoard.board;
        QComment qComment = QComment.comment;
        QComment qSubComment = new QComment("subComment");
        QUser qBoardUser = QUser.user;
        QUser qCommentUser = new QUser("commentUser");
        QUser qSubCommentUser = new QUser("subCommentUser");

        Board result = queryFactory
                .selectDistinct(qBoard)
                .from(qBoard)
                .leftJoin(qBoard.boardUser, qBoardUser).fetchJoin()
                .leftJoin(qBoard.comments, qComment).fetchJoin()
                .leftJoin(qComment.commentUser, qCommentUser).fetchJoin()
                .leftJoin(qComment.subComments, qSubComment).fetchJoin()
                .leftJoin(qSubComment.commentUser, qSubCommentUser).fetchJoin()
                .where(qBoard.boardId.eq(boardId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<Board> findAllByBoardType(String boardType) {
        QBoard qBoard = QBoard.board;

        return queryFactory
                .selectFrom(qBoard)
                .where(
                        qBoard.boardType.eq(boardType),
                        qBoard.boardIsDeleted.eq(false)
                )
                .orderBy(
                        qBoard.isPinned.desc(), // true 값이 먼저 오도록 desc 사용
                        qBoard.boardCreatedAt.desc() // 최신 글이 먼저 오도록 desc 사용
                )
                .fetch();
    }
}
