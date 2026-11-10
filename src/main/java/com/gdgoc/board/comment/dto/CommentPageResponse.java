package com.gdgoc.board.comment.dto;

import com.gdgoc.board.comment.domain.Comment;
import org.springframework.data.domain.Page;

import java.util.List;

public record CommentPageResponse(List<CommentResponse> comments, int page, int totalPages,
                                  long totalElements, boolean hasNext) {

    public static CommentPageResponse from(Page<Comment> commentPage) {
        return new CommentPageResponse(
                commentPage.getContent().stream().map(CommentResponse::from).toList(),
                commentPage.getNumber(), commentPage.getTotalPages(),
                commentPage.getTotalElements(), commentPage.hasNext());
    }
}
