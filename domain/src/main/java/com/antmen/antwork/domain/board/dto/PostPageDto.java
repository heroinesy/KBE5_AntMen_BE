package com.antmen.antwork.domain.board.dto;






@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostPageDto {
    private List<BoardListResponseDto> pinnedPosts;
    private Page<BoardListResponseDto> posts;
}
