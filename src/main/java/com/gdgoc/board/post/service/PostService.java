package com.gdgoc.board.post.service;

import com.gdgoc.board.post.domain.Post;
import com.gdgoc.board.post.dto.PostCreateRequest;
import com.gdgoc.board.post.dto.PostResponse;
import com.gdgoc.board.post.dto.PostUpdateRequest;
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

    // TODO [세션 04 · 3-5] 필드 추가: private final CommentRepository commentRepository;

    @Transactional
    public PostResponse create(PostCreateRequest request) {
        Post post = postRepository.save(request.toEntity());
        return PostResponse.from(post);
    }

    // TODO [세션 04 · 4-2] 기존 findAll() 수정: Pageable 을 받아 PostPageResponse 반환 (PostPageResponse 는 dto 새 파일)
    public List<PostResponse> findAll() {
        return postRepository.findAll().stream()
                .map(PostResponse::from)
                .toList();
    }

    public PostResponse findById(Long postId) {
        return PostResponse.from(getPost(postId));
    }

    @Transactional
    public PostResponse update(Long postId, PostUpdateRequest request) {
        Post post = getPost(postId);
        post.update(request.title(), request.content());
        return PostResponse.from(post);
    }

    // TODO [세션 04 · 3-5] 기존 delete() 수정: 게시글을 지우기 전에 commentRepository.deleteByPostId(postId) 호출
    @Transactional
    public void delete(Long postId) {
        Post post = getPost(postId);
        postRepository.delete(post);
    }

    public List<PostResponse> search(String keyword) {
        return postRepository.findByTitleContaining(keyword).stream()
                .map(PostResponse::from)
                .toList();
    }

    private Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다. id=" + postId));
    }
}
