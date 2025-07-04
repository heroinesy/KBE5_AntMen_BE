package com.antmen.antwork.common.infra.repository.board;

import com.antmen.antwork.common.api.response.board.BoardListResponseDto;
import com.antmen.antwork.common.domain.entity.Board;
import com.antmen.antwork.common.domain.entity.QBoard;
import com.antmen.antwork.common.domain.entity.QComment;
import com.antmen.antwork.common.domain.entity.account.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

//    @Override
//    public Optional<Board> findByIdWithCommentsAndSubComments(Long boardId) {
//        QBoard qBoard = QBoard.board;
//        QComment qComment = QComment.comment;
//        QComment qSubComment = new QComment("subComment");
//        QUser qBoardUser = QUser.user;
//        QUser qCommentUser = new QUser("commentUser");
//        QUser qSubCommentUser = new QUser("subCommentUser");
//
//        Board result = queryFactory
//                .selectDistinct(qBoard)
//                .from(qBoard)
//                .leftJoin(qBoard.boardUser, qBoardUser).fetchJoin()
//                .leftJoin(qBoard.comments, qComment).fetchJoin()
//                .leftJoin(qComment.commentUser, qCommentUser).fetchJoin()
//                .leftJoin(qComment.subComments, qSubComment).fetchJoin()
//                .leftJoin(qSubComment.commentUser, qSubCommentUser).fetchJoin()
//                .where(qBoard.boardId.eq(boardId))
//                .fetchOne();
//
//        return Optional.ofNullable(result);
//    }

    @Override
    public Optional<Board> findByIdWithCommentsAndSubComments(Long boardId) {
        return Optional.empty();
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

    @Override
    public List<BoardListResponseDto> findAllByBoardTypeAndIsPinnedIsTrue(String boardType) {
        QBoard qBoard = QBoard.board;
        QComment qComment = QComment.comment;
        QUser qUser = QUser.user;

        BooleanBuilder whereCondition = new BooleanBuilder();
        whereCondition.and(qBoard.boardType.eq(boardType));
        whereCondition.and(qBoard.boardIsDeleted.eq(false));
        whereCondition.and(qBoard.isPinned.eq(true));

        List<BoardListResponseDto> content = queryFactory
                .select(Projections.constructor(BoardListResponseDto.class,
                        qBoard.boardId,
                        qUser.userName,
                        qBoard.boardTitle,
                        qBoard.boardCreatedAt,
                        qBoard.boardModifiedAt,
                        qComment.count()
                ))
                .from(qBoard)
                .leftJoin(qUser).on(qBoard.boardUserId.eq(qUser.userId))
                .leftJoin(qComment).on(qBoard.boardId.eq(qComment.boardId)
                        .and(qComment.commentIsDeleted.eq(false))
                )
                .where(whereCondition)
                .groupBy(
                        qBoard.boardId,
                        qUser.userName,
                        qBoard.boardTitle,
                        qBoard.boardCreatedAt,
                        qBoard.boardModifiedAt
                )
                .orderBy(qBoard.boardModifiedAt.desc())
                .fetch();

        return content;
    }

    @Override
    public Page<BoardListResponseDto> searchBoardsWithPaging(String boardType, Long userId, String name, String sortBy, Pageable pageable) {
        QBoard qBoard = QBoard.board;
        QComment qComment = QComment.comment;
        QUser qUser = QUser.user;

        BooleanBuilder whereCondition = new BooleanBuilder();
        whereCondition.and(qBoard.boardType.eq(boardType));
        whereCondition.and(qBoard.boardIsDeleted.eq(false));
        whereCondition.and(qBoard.isPinned.eq(false));

        if (userId != null) {
            whereCondition.and(qBoard.boardUserId.eq(userId));
        }

        if (name != null && !name.trim().isEmpty()) {
            whereCondition.and(
                    qBoard.boardTitle.containsIgnoreCase(name)
                            .or(qBoard.boardContent.containsIgnoreCase(name))
            );
        }

        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        if ("latest".equals(sortBy)) {
            orderSpecifiers.add(qBoard.boardCreatedAt.desc());
        } else if ("oldest".equals(sortBy)) {
            orderSpecifiers.add(qBoard.boardCreatedAt.asc());
            
        } else if ("waiting".equals(sortBy)) {
            orderSpecifiers.add(qBoard.isFinished.desc());
        }

        List<BoardListResponseDto> content = queryFactory
                .select(Projections.constructor(BoardListResponseDto.class,
                        qBoard.boardId,
                        qUser.userName,
                        qBoard.boardTitle,
                        qBoard.boardCreatedAt,
                        qBoard.boardModifiedAt,
                        qComment.count()
                ))
                .from(qBoard)
                .leftJoin(qUser).on(qBoard.boardUserId.eq(qUser.userId))
                .leftJoin(qComment).on(qBoard.boardId.eq(qComment.boardId)
                        .and(qComment.commentIsDeleted.eq(false))
                )
                .where(whereCondition)
                .groupBy(
                        qBoard.boardId,
                        qUser.userName,
                        qBoard.boardTitle,
                        qBoard.boardCreatedAt,
                        qBoard.boardModifiedAt
                )
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(qBoard.count())
                .from(qBoard)
                .where(whereCondition)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
