package com.antmen.antwork.common.domain.entity;

import com.antmen.antwork.common.api.request.board.BoardRequestDto;
import com.antmen.antwork.common.domain.entity.account.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "board")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(nullable = false)
    private Long boardUserId;

    @Column(nullable = false)
    private String boardType;       // customerNotice, customerPersonal...

    @Column(nullable = false)
    private String boardTitle;

    @Column(nullable = false)
    private String boardContent;

    @Column(name = "board_created_at", nullable = false, updatable = false)
    private LocalDateTime boardCreatedAt;

    @Column(name = "board_modified_at", nullable = false)
    private LocalDateTime boardModifiedAt;

    private LocalDateTime boardReservedAt;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isPinned;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean boardIsDeleted;

    @Column(nullable = false)
    private Boolean isFinished;

}