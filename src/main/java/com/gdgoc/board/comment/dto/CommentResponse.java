package com.gdgoc.board.comment.dto;

import com.gdgoc.board.comment.domain.Comment;

import java.time.LocalDateTime;

public record CommentResponse(Long id, String content, Long postId, LocalDateTime createdAt) {

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getContent(),
                comment.getPost().getId(), comment.getCreatedAt());
    }
}
