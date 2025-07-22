package com.antmen.antwork.domain.board.repository;


import com.antmen.antwork.domain.board.dto.BoardListResponseDto;
import com.antmen.antwork.domain.board.entity.Board;
import com.antmen.antwork.domain.board.entity.BoardStatus;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
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
                        qComment.count(),
                        qBoard.boardStatus,
                        qBoard.boardIsDeleted
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
                        qBoard.boardModifiedAt,
                        qBoard.boardStatus,
                        qBoard.boardIsDeleted
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
            orderSpecifiers.add(
                    new CaseBuilder()
                            .when(qBoard.boardStatus.eq(BoardStatus.InProgress)).then(1)
                            .when(qBoard.boardStatus.eq(BoardStatus.New)).then(2)
                            .when(qBoard.boardStatus.eq(BoardStatus.Resolved)).then(3)
                            .otherwise(4)
                            .asc()
            );
        }

        List<BoardListResponseDto> content = queryFactory
                .select(Projections.constructor(BoardListResponseDto.class,
                        qBoard.boardId,
                        qUser.userName,
                        qBoard.boardTitle,
                        qBoard.boardCreatedAt,
                        qBoard.boardModifiedAt,
                        qComment.count(),
                        qBoard.boardStatus,
                        qBoard.boardIsDeleted
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
                        qBoard.boardModifiedAt,
                        qBoard.boardStatus,
                        qBoard.boardIsDeleted
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

    @Override
    public List<BoardListResponseDto> searchCustomerNoticesWithPaging(String name, String sortBy) {
        QBoard qBoard = QBoard.board;
        QComment qComment = QComment.comment;
        QUser qUser = QUser.user;

        BooleanBuilder whereCondition = new BooleanBuilder();
        whereCondition.and(qBoard.boardType.eq("customer-notice"));
        whereCondition.or(qBoard.boardType.eq("customer").and(qBoard.isPinned.eq(true)));

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
        }

        List<BoardListResponseDto> content = queryFactory
                .select(Projections.constructor(BoardListResponseDto.class,
                        qBoard.boardId,
                        qUser.userName,
                        qBoard.boardTitle,
                        qBoard.boardCreatedAt,
                        qBoard.boardModifiedAt,
                        qComment.count(),
                        qBoard.boardStatus,
                        qBoard.boardIsDeleted
                ))
                .from(qBoard)
                .leftJoin(qUser).on(qUser.userId.eq(qBoard.boardUserId))
                .leftJoin(qComment).on(qBoard.boardId.eq(qComment.boardId)
                        .and(qComment.commentIsDeleted.eq(false))
                )
                .where(whereCondition)
                .groupBy(
                        qBoard.boardId,
                        qUser.userName,
                        qBoard.boardTitle,
                        qBoard.boardCreatedAt,
                        qBoard.boardModifiedAt,
                        qBoard.boardStatus,
                        qBoard.boardIsDeleted
                )
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .fetch();

        return content;
    }

    @Override
    public List<BoardListResponseDto> searchManagerNoticesWithPaging(String name, String sortBy) {
        QBoard qBoard = QBoard.board;
        QComment qComment = QComment.comment;
        QUser qUser = QUser.user;

        BooleanBuilder whereCondition = new BooleanBuilder();
        whereCondition.and(qBoard.boardType.eq("manager-notice"));
        whereCondition.or(qBoard.boardType.eq("manager").and(qBoard.isPinned.eq(true)));

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
        }

        List<BoardListResponseDto> content = queryFactory
                .select(Projections.constructor(BoardListResponseDto.class,
                        qBoard.boardId,
                        qUser.userName,
                        qBoard.boardTitle,
                        qBoard.boardCreatedAt,
                        qBoard.boardModifiedAt,
                        qComment.count(),
                        qBoard.boardStatus,
                        qBoard.boardIsDeleted
                ))
                .from(qBoard)
                .leftJoin(qUser).on(qUser.userId.eq(qBoard.boardUserId))
                .leftJoin(qComment).on(qBoard.boardId.eq(qComment.boardId)
                        .and(qComment.commentIsDeleted.eq(false))
                )
                .where(whereCondition)
                .groupBy(
                        qBoard.boardId,
                        qUser.userName,
                        qBoard.boardTitle,
                        qBoard.boardCreatedAt,
                        qBoard.boardModifiedAt,
                        qBoard.boardStatus,
                        qBoard.boardIsDeleted
                )
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .fetch();

        return content;
    }

    @Override
    public List<BoardListResponseDto> searchPersonalBoardsWithPaging(String boardType, String name, String sortBy) {
        QBoard qBoard = QBoard.board;
        QComment qComment = QComment.comment;
        QUser qUser = QUser.user;

        BooleanBuilder whereCondition = new BooleanBuilder();
        whereCondition.and(qBoard.boardType.eq(boardType));
        whereCondition.and(qBoard.isPinned.eq(false));

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
        }

        List<BoardListResponseDto> content = queryFactory
                .select(Projections.constructor(BoardListResponseDto.class,
                        qBoard.boardId,
                        qUser.userName,
                        qBoard.boardTitle,
                        qBoard.boardCreatedAt,
                        qBoard.boardModifiedAt,
                        qComment.count(),
                        qBoard.boardStatus,
                        qBoard.boardIsDeleted
                ))
                .from(qBoard)
                .leftJoin(qUser).on(qUser.userId.eq(qBoard.boardUserId))
                .leftJoin(qComment).on(qBoard.boardId.eq(qComment.boardId)
                        .and(qComment.commentIsDeleted.eq(false))
                )
                .where(whereCondition)
                .groupBy(
                        qBoard.boardId,
                        qUser.userName,
                        qBoard.boardTitle,
                        qBoard.boardCreatedAt,
                        qBoard.boardModifiedAt,
                        qBoard.boardStatus,
                        qBoard.boardIsDeleted
                )
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .fetch();

        return content;
    }

}
