package com.gdgoc.board.post.dto;

import com.gdgoc.board.post.domain.Post;

// TODO [세션 05 · 3-2] 필드에 규칙 추가: title → @NotBlank + @Size(max = 100), content → @NotBlank
public record PostCreateRequest(String title, String content) {

    public Post toEntity() {
        return new Post(title, content);
    }
}
