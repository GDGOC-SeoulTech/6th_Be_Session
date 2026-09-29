package com.gdgoc.board.post.controller;

import com.gdgoc.board.post.dto.PostCreateRequest;
import com.gdgoc.board.post.dto.PostResponse;
import com.gdgoc.board.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> create(@RequestBody PostCreateRequest request) {
        PostResponse response = postService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> findAll() {
        return ResponseEntity.ok(postService.findAll());
    }

    // TODO [세션 03 · 3-1] 게시글 단건 조회 API: GET /posts/{postId}

    // TODO [세션 03 · 3-2] 게시글 수정 API: PATCH /posts/{postId}

    // TODO [세션 03 · 3-3] 게시글 삭제 API: DELETE /posts/{postId}

    // TODO [세션 03 · 7] 게시글 검색 API: GET /posts/search?keyword= (과제)
}
