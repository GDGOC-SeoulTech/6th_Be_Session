package com.gdgoc.board.post.dto;

// TODO [세션 05 · 3-2] 필드에 규칙 추가: title → @NotBlank + @Size(max = 100), content → @NotBlank
public record PostUpdateRequest(String title, String content) {
}
