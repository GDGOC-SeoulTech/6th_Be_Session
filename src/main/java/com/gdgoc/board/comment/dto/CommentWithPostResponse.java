package com.gdgoc.board.comment.dto;

import com.gdgoc.board.comment.domain.Comment;

public record CommentWithPostResponse(Long commentId, String content, String postTitle) {

    public static CommentWithPostResponse from(Comment comment) {
        return new CommentWithPostResponse(comment.getId(), comment.getContent(),
                comment.getPost().getTitle());
    }
}
