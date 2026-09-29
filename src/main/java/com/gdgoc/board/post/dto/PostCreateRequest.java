package com.gdgoc.board.post.dto;

import com.gdgoc.board.post.domain.Post;

public record PostCreateRequest(String title, String content) {

    public Post toEntity() {
        return new Post(title, content);
    }
}
