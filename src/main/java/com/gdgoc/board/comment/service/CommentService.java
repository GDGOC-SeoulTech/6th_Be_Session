package com.gdgoc.board.comment.service;

import com.gdgoc.board.comment.domain.Comment;
import com.gdgoc.board.comment.dto.CommentCreateRequest;
import com.gdgoc.board.comment.dto.CommentPageResponse;
import com.gdgoc.board.comment.dto.CommentResponse;
import com.gdgoc.board.comment.dto.CommentWithPostResponse;
import com.gdgoc.board.comment.repository.CommentRepository;
import com.gdgoc.board.post.domain.Post;
import com.gdgoc.board.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    // TODO [세션 05 · 4-3] IllegalArgumentException → new CustomException(ErrorCode.POST_NOT_FOUND)
    @Transactional
    public CommentResponse create(Long postId, CommentCreateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다. id=" + postId));
        Comment comment = commentRepository.save(new Comment(request.content(), post));
        return CommentResponse.from(comment);
    }

    public CommentPageResponse findByPost(Long postId, Pageable pageable) {
        return CommentPageResponse.from(commentRepository.findByPostId(postId, pageable));
    }

    // TODO [세션 05 · 4-3] IllegalArgumentException → new CustomException(ErrorCode.COMMENT_NOT_FOUND)
    @Transactional
    public void delete(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 없습니다. id=" + commentId));
        commentRepository.delete(comment);
    }

    public List<CommentWithPostResponse> findAllWithPost() {
        return commentRepository.findAllWithPost().stream()
                .map(CommentWithPostResponse::from)
                .toList();
    }
}
