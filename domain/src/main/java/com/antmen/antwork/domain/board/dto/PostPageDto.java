package com.antmen.antwork.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
