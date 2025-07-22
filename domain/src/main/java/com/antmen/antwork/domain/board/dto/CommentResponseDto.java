package com.antmen.antwork.domain.board.dto;









@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponseDto {
    private Long commentId;
    private Long userId;
    private String userName;
    private String userAvatar;
    private String commentContent;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> subComments;
} 