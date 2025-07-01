package com.antmen.antwork.common.api.response.board;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostPageDto {
    private List<BoardListResponseDto> pinnedPosts;
    private Page<BoardListResponseDto> posts;
}
