package com.gdgoc.board.post.dto;

import com.gdgoc.board.post.domain.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostCreateRequest(
        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 100, message = "제목은 100자 이하로 입력해주세요.")
        String title,

        @NotBlank(message = "내용은 필수입니다.")
        String content
) {
    public Post toEntity() {
        return new Post(title, content);
    }
}
