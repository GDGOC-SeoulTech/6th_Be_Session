package com.gdgoc.board.post.service;

import com.gdgoc.board.comment.repository.CommentRepository;
import com.gdgoc.board.global.exception.CustomException;
import com.gdgoc.board.global.exception.ErrorCode;
import com.gdgoc.board.post.domain.Post;
import com.gdgoc.board.post.dto.PostCreateRequest;
import com.gdgoc.board.post.dto.PostPageResponse;
import com.gdgoc.board.post.dto.PostResponse;
import com.gdgoc.board.post.dto.PostUpdateRequest;
import com.gdgoc.board.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    @Transactional
    public PostResponse create(PostCreateRequest request) {
        Post post = postRepository.save(request.toEntity());
        return PostResponse.from(post);
    }

    public PostPageResponse findAll(Pageable pageable) {
        return PostPageResponse.from(postRepository.findAll(pageable));
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

    @Transactional
    public void delete(Long postId) {
        Post post = getPost(postId);
        commentRepository.deleteByPostId(postId);
        postRepository.delete(post);
    }

    public List<PostResponse> search(String keyword) {
        return postRepository.findByTitleContaining(keyword).stream()
                .map(PostResponse::from)
                .toList();
    }

    @Transactional
    public PostResponse like(Long postId) {
        Post post = getPost(postId);
        post.like();
        return PostResponse.from(post);
    }

    private Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }
}
