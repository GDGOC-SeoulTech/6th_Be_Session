package com.gdgoc.board.comment.controller;

import com.gdgoc.board.comment.dto.CommentCreateRequest;
import com.gdgoc.board.comment.dto.CommentPageResponse;
import com.gdgoc.board.comment.dto.CommentResponse;
import com.gdgoc.board.comment.dto.CommentWithPostResponse;
import com.gdgoc.board.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> create(@PathVariable Long postId,
                                                  @RequestBody CommentCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.create(postId, request));
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentPageResponse> findByPost(
            @PathVariable Long postId,
            @ParameterObject
            @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(commentService.findByPost(postId, pageable));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.noContent().build();
    }

    // TODO [세션 04 · 5-3] 전체 댓글 조회 API: GET /comments
    @GetMapping("/comments")
    public ResponseEntity<List<CommentWithPostResponse>> findAllWithPost() {
        return ResponseEntity.ok(commentService.findAllWithPost());
    }
}