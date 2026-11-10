package com.gdgoc.board.comment.repository;

import com.gdgoc.board.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // TODO [세션 04 · 3-2] 게시글별 댓글 조회 findByPostId(postId)
    Page<Comment> findByPostId(Long postId, Pageable pageable);
    // TODO [세션 04 · 3-5] 게시글의 댓글 전체 삭제 deleteByPostId(postId)
    void deleteByPostId(Long postId);
    // TODO [세션 04 · 5-4] fetch join 으로 댓글 + 게시글 한 번에 조회 findAllWithPost() (@Query)
    @Query("SELECT c FROM Comment c JOIN FETCH c.post")
    List<Comment> findAllWithPost();
}
