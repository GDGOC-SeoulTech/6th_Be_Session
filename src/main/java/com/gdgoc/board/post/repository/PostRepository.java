package com.gdgoc.board.post.repository;

import com.gdgoc.board.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByTitleContaining(String keyword);
}
