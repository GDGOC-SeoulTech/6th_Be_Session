package com.gdgoc.board.comment.dto;

// TODO [세션 05 · 3-2] 필드에 규칙 추가: content → @NotBlank + @Size(max = 500)
public record CommentCreateRequest(String content) {
}
