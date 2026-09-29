package com.gdgoc.board.post.dto;

import com.gdgoc.board.post.domain.Post;

import java.time.LocalDateTime;

public record PostResponse(Long id, String title, String content, LocalDateTime createdAt) {

    public static PostResponse from(Post post) {
        return new PostResponse(post.getId(), post.getTitle(), post.getContent(), post.getCreatedAt());
    }
}
