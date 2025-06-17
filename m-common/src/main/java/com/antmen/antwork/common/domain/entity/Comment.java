package com.antmen.antwork.common.domain.entity;

import com.antmen.antwork.common.domain.entity.account.User;
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

    @Column(nullable = false)
    private Long boardId;

    @Column(nullable = false)
    private Long commentUserId;

    @Column(nullable = false)
    private String commentContent;

    @Column(nullable = false, updatable = false)
    private LocalDateTime commentCreatedAt;

    @Column(nullable = false)
    private LocalDateTime commentModifiedAt;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean commentIsDeleted;

    private Long commentParentId;

}
