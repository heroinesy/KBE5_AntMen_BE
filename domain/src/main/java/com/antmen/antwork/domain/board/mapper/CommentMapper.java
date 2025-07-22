package com.antmen.antwork.domain.board.mapper;










@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final CommentRepository commentRepository;

    public Comment toEntity(Long userId, Long boardId, CommentRequestDto commentRequestDto) {
            return Comment.builder()
                    .commentUserId(userId)
                    .boardId(boardId)
                    .commentContent(commentRequestDto.getContent())
                    .commentCreatedAt(LocalDateTime.now())
                    .commentModifiedAt(null)
                    .commentParentId(commentRequestDto.getParentId())
                    .commentIsDeleted(false)
                    .build();
        }
    }

//    public CommentResponseDto toResponseDto(Comment comment) {
//        if (comment == null) {
//            return null;
//        }
//
//        return CommentResponseDto.builder()
//                .commentId(comment.getCommentId())
//                .userName(comment.getCommentUser() != null ? comment.getCommentUser().getUserName() : null)
//                .commentContent(comment.getCommentContent())
//                .createdAt(comment.getCommentCreatedAt())
//                .modifiedAt(comment.getCommentModifiedAt())
//                .build();
//    }
//}
