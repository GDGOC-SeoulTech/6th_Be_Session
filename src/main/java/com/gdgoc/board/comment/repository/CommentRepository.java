package com.gdgoc.board.comment.repository;

import com.gdgoc.board.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByPostId(Long postId, Pageable pageable);

    void deleteByPostId(Long postId);

    @Query("SELECT c FROM Comment c JOIN FETCH c.post")
    List<Comment> findAllWithPost();
}
