package com.antmen.antwork.domain.board.dto;






@Getter
@Setter
@ToString
@Builder
public class CommentRequestDto {
    private String content;
    private Long parentId;
}
