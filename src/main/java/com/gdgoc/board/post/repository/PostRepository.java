package com.gdgoc.board.post.repository;

import com.gdgoc.board.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    // TODO [세션 03 · 7] 제목에 검색어가 포함된 게시글 조회 (과제)
}
