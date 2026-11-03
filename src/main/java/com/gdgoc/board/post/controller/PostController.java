package com.gdgoc.board.post.controller;

import com.gdgoc.board.post.dto.PostCreateRequest;
import com.gdgoc.board.post.dto.PostResponse;
import com.gdgoc.board.post.dto.PostUpdateRequest;
import com.gdgoc.board.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    // TODO [세션 04 · 4-2] 기존 findAll() 수정: @PageableDefault + @ParameterObject Pageable 을 받아 PostPageResponse 반환
    @GetMapping
    public ResponseEntity<List<PostResponse>> findAll() {
        return ResponseEntity.ok(postService.findAll());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> findById(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.findById(postId));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponse> update(@PathVariable Long postId,
                                               @RequestBody PostUpdateRequest request) {
        return ResponseEntity.ok(postService.update(postId, request));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(@PathVariable Long postId) {
        postService.delete(postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostResponse>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(postService.search(keyword));
    }
}
