package com.antmen.antwork.domain.board.entity;










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

    private LocalDateTime commentModifiedAt;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean commentIsDeleted;

    private Long commentParentId;

    @OneToMany
    @JoinColumn(name = "commentParentId", referencedColumnName = "commentId")
    @Where(clause = "comment_is_deleted = false")
    private List<Comment> subComments;

}
