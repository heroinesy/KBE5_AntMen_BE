package com.antmen.antwork.common.domain.entity;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User boardUser;

    @Column(nullable = false)
    private String boardType;

    @Column(nullable = false)
    private String boardTitle;

    @Column(nullable = false)
    private String boardContent;

//    @CreationTimestamp
    @Column(name = "board_created_at", nullable = false, updatable = false)
    private LocalDateTime boardCreatedAt;

//    @UpdateTimestamp
    @Column(name = "board_modified_at", nullable = false)
    private LocalDateTime boardModifiedAt;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isPinned;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean boardIsDeleted;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private List<Comment> comments;

}