package com.gdgoc.board.comment.controller;

import com.gdgoc.board.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // TODO [세션 04 · 3-4] 댓글 작성 POST /posts/{postId}/comments, 목록 GET /posts/{postId}/comments, 삭제 DELETE /comments/{commentId}

    // TODO [세션 04 · 5-3] 전체 댓글 조회 API: GET /comments
}
