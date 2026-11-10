package com.gdgoc.board.comment.service;

import com.gdgoc.board.comment.domain.Comment;
import com.gdgoc.board.comment.dto.CommentCreateRequest;
import com.gdgoc.board.comment.dto.CommentResponse;
import com.gdgoc.board.comment.dto.CommentWithPostResponse;
import com.gdgoc.board.comment.repository.CommentRepository;
import com.gdgoc.board.post.domain.Post;
import com.gdgoc.board.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// src/main/java/com/gdgoc/board/comment/service/CommentService.java
// → TODO [세션 04 · 3-3] 자리에 작성
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentResponse create(Long postId, CommentCreateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다. id=" + postId));
        Comment comment = commentRepository.save(new Comment(request.content(), post));
        return CommentResponse.from(comment);
    }

    public List<CommentResponse> findByPost(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(CommentResponse::from)
                .toList();
    }

    @Transactional
    public void delete(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 없습니다. id=" + commentId));
        commentRepository.delete(comment);
    }


    // TODO [세션 04 · 5-3] 전체 댓글 + 게시글 제목 조회 findAllWithPost()
    //   (dto 새 파일: comment/dto/CommentWithPostResponse) → 5-4 에서 fetch join 으로 수정
    public List<CommentWithPostResponse> findAllWithPost() {
        return commentRepository.findAllWithPost().stream()
                .map(CommentWithPostResponse::from)
                .toList();
    }
}