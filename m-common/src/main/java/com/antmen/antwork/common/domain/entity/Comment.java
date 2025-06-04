package com.antmen.antwork.common.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User commentUser;

    @Column(nullable = false)
    private String commentContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime commentCreatedAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime commentModifiedAt;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean commentIsDeleted;

    @OneToMany(
            mappedBy = "parentComment",
            fetch = FetchType.LAZY
    )
    private List<Comment> subComments;

}
