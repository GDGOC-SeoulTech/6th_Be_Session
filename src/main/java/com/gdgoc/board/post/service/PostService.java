package com.gdgoc.board.post.service;

import com.gdgoc.board.post.domain.Post;
import com.gdgoc.board.post.dto.PostCreateRequest;
import com.gdgoc.board.post.dto.PostResponse;
import com.gdgoc.board.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public PostResponse create(PostCreateRequest request) {
        Post post = postRepository.save(request.toEntity());
        return PostResponse.from(post);
    }

    public List<PostResponse> findAll() {
        return postRepository.findAll().stream()
                .map(PostResponse::from)
                .toList();
    }

    // TODO [세션 03 · 3-1] 게시글 단건 조회 findById(postId)

    // TODO [세션 03 · 3-2] 게시글 수정 update(postId, request)

    // TODO [세션 03 · 3-3] 게시글 삭제 delete(postId)

    // TODO [세션 03 · 7] 게시글 검색 search(keyword) (과제)
}
