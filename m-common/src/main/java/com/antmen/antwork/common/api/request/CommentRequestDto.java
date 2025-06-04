package com.antmen.antwork.common.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class CommentRequestDto {
    private String content;
    private Long parentId;
}
